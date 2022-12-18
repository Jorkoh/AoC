package year2022.day16

import utils.Solution

fun main() {
    with(Day16()) {
        testFile(::part1, 1651)
        testFile(::part2, 1707)
        solve() // TODO part 2 takes 8 minutes to run as it is, shouldn't be hard to improve
    }
}

class Day16 : Solution() {
    override val day = 16
    override val year = 2022

    private val regex = Regex("""Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""")

    private data class Tunnel(val toId: String, val cost: Int)
    private data class Room(val id: String, val flow: Int, val tunnels: List<Tunnel>)

    // The ones that have a flow rate of 0 are never worth turning
    override fun part1(): Any {
        val allRooms = parseRooms()
        val withValve = allRooms.filter { it.flow > 0 }.map { it.id }.toMutableSet()
        val start = allRooms.first { it.id == "AA" }.connectTo(withValve, allRooms)
        val relevantConnected = allRooms.filter { it.flow > 0 }.map { room ->
            room.connectTo(withValve, allRooms)
        }

        return start.tunnels.maxOf { tunnel ->
            relevantConnected.bestScoreAlone(
                currentId = tunnel.toId,
                timeRemaining = 30 - tunnel.cost,
                scoreSoFar = 0,
                unopenedVales = withValve
            )
        }
    }

    override fun part2(): Any {
        val allRooms = parseRooms()
        val withValve = allRooms.filter { it.flow > 0 }.map { it.id }.toMutableSet()
        val start = allRooms.first { it.id == "AA" }.connectTo(withValve, allRooms)
        val relevantConnected = allRooms.filter { it.flow > 0 }.map { room ->
            room.connectTo(withValve, allRooms)
        }

        return start.tunnels.maxOf { tunnelA ->
            start.tunnels.filter { it.toId != tunnelA.toId }.maxOf { tunnelB ->
                relevantConnected.bestScoreWithHelper(
                    currentIdA = tunnelA.toId,
                    currentIdB = tunnelB.toId,
                    timeA = 26 - tunnelA.cost,
                    timeB = 26 - tunnelB.cost,
                    scoreSoFarA = 0,
                    scoreSoFarB = 0,
                    unopenedVales = withValve
                )
            }
        }
    }

    private fun parseRooms() = input.map { l ->
        val (id, flowRaw, tunnelsRaw) = regex.find(l)!!.groupValues.drop(1)
        Room(
            id = id,
            flow = flowRaw.toInt(),
            tunnels = tunnelsRaw.split(", ").map { id -> Tunnel(id, 1) }
        )
    }

    private fun Room.connectTo(targetIds: Set<String>, rooms: List<Room>): Room {
        // Find cost to locations with BFS
        val idToCost = mutableMapOf(id to 0)
        val frontier = ArrayDeque(listOf(this))
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            current.tunnels.filter { it.toId !in idToCost }.forEach { tunnel ->
                val newRoom = rooms.first { room -> room.id == tunnel.toId }
                idToCost[newRoom.id] = idToCost[current.id]!! + 1
                frontier.addLast(newRoom)
            }
        }
        return copy(
            tunnels = idToCost
                .filter { (id, _) -> id != this.id && id in targetIds }
                .map { (id, cost) -> Tunnel(id, cost) }
        )
    }

    // Find best score with DFS
    private fun List<Room>.bestScoreAlone(
        currentId: String,
        timeRemaining: Int,
        scoreSoFar: Int,
        unopenedVales: MutableSet<String>
    ): Int {
        if (timeRemaining <= 0) return scoreSoFar // Time run out in the tunnel or while turning this valve

        val current = first { it.id == currentId }
        val newScore = scoreSoFar + (timeRemaining - 1) * current.flow
        val options = current.tunnels.filter { it.toId in unopenedVales }

        if (options.isEmpty()) return newScore // No valves left to turn

        return options.maxOf { tunnel ->
            bestScoreAlone(
                currentId = tunnel.toId,
                timeRemaining = timeRemaining - 1 - tunnel.cost,
                scoreSoFar = newScore,
                unopenedVales = unopenedVales.apply { remove(currentId) }
            ).also {
                unopenedVales.add(currentId)
            }
        }
    }

    // Find best score with DFS
    private fun List<Room>.bestScoreWithHelper(
        currentIdA: String, // Walking towards
        currentIdB: String, // Walking towards
        timeA: Int, // When A arrives
        timeB: Int, // When B arrives
        scoreSoFarA: Int, // Score accumulated by A
        scoreSoFarB: Int, // Score accumulated by B
        unopenedVales: MutableSet<String> // Remaining valves
    ): Int {
        // Neither has time to turn a valve (negative means time ran out in tunnel)
        if (timeA <= 0 && timeB <= 0) return scoreSoFarA + scoreSoFarB

        // Process the first one to arrive, if tie main
        val turnA = timeA >= timeB
        val currentId = if (turnA) currentIdA else currentIdB
        val current = first { it.id == currentId }
        val scoreSoFar = if (turnA) scoreSoFarA else scoreSoFarB
        val time = if (turnA) timeA else timeB
        val newScore = scoreSoFar + (time - 1) * current.flow
        val options = current.tunnels.filter { t ->
            t.toId in unopenedVales && t.toId != currentIdA && t.toId != currentIdB
        }

        return if (options.isNotEmpty()) {
            options.maxOf { tunnel ->
                bestScoreWithHelper(
                    currentIdA = if (turnA) tunnel.toId else currentIdA,
                    currentIdB = if (!turnA) tunnel.toId else currentIdB,
                    timeA = if (turnA) time - 1 - tunnel.cost else timeA,
                    timeB = if (!turnA) time - 1 - tunnel.cost else timeB,
                    scoreSoFarA = if (turnA) newScore else scoreSoFarA,
                    scoreSoFarB = if (!turnA) newScore else scoreSoFarB,
                    unopenedVales = unopenedVales.apply { remove(currentId) }
                ).also { unopenedVales.add(currentId) }
            }
        } else {
            // No valves left to turn but may still need to process 
            // the other so drop this one to negative time :^)
            return bestScoreWithHelper(
                currentIdA = currentIdA,
                currentIdB = currentIdB,
                timeA = if (turnA) -99999 else timeA,
                timeB = if (!turnA) -99999 else timeB,
                scoreSoFarA = if (turnA) newScore else scoreSoFarA,
                scoreSoFarB = if (!turnA) newScore else scoreSoFarB,
                unopenedVales = unopenedVales.apply { remove(currentId) }
            ).also { unopenedVales.add(currentId) }
        }
    }
}