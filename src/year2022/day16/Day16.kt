package year2022.day16

import utils.Solution

fun main() {
    with(Day16()) {
        testFile(::part1, 1651)
//        testFile(::part2, Unit)
        solve()
        benchmark()
    }
}

class Day16 : Solution() {
    override val day = 16
    override val year = 2022

    private val regex = Regex("""Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""")

    private data class Tunnel(val towardsId: String, val cost: Int)
    private data class Room(val id: String, val flow: Int, val tunnels: List<Tunnel>)

    // The ones that have a flow rate of 0 are never worth turning
    override fun part1(): Any {
        val allRooms = input.map { l ->
            val (id, flowRaw, tunnelsRaw) = regex.find(l)!!.groupValues.drop(1)
            Room(
                id = id,
                flow = flowRaw.toInt(),
                tunnels = tunnelsRaw.split(", ").map { id -> Tunnel(id, 1) }
            )
        }

        val withValve = allRooms.filter { it.flow > 0 }.map { it.id }.toMutableSet()
        val start = allRooms.first { it.id == "AA" }.connectTo(withValve, allRooms)
        val relevantConnected = allRooms.filter { it.id in withValve }.map { room ->
            room.connectTo(withValve, allRooms)
        }
        return start.tunnels.maxOf { tunnel ->
            val score = relevantConnected.bestRelease(
                currentId = tunnel.towardsId,
                timeRemaining = 30 - tunnel.cost,
                scoreSoFar = 0,
                unopenedVales = withValve
            )
            score
        }
    }

    override fun part2(): Any {
        return Unit
    }

    private fun Room.connectTo(targetIds: Set<String>, rooms: List<Room>): Room {
        // Find cost to locations with BFS
        val idToCost = mutableMapOf(id to 0)
        val frontier = ArrayDeque(listOf(this))
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            current.tunnels.filter { it.towardsId !in idToCost }.forEach { tunnel ->
                val newRoom = rooms.first { room -> room.id == tunnel.towardsId }
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
    private fun List<Room>.bestRelease(
        currentId: String,
        timeRemaining: Int,
        scoreSoFar: Int,
        // Make this a mutable list and remove and re-add to improve speed
        unopenedVales: MutableSet<String>
    ): Int {
        if (timeRemaining <= 0) return scoreSoFar // Time run out in the tunnel or while turning this valve

        val current = first { it.id == currentId }
        val newScore = scoreSoFar + (timeRemaining - 1) * current.flow
        val options = current.tunnels.filter { it.towardsId in unopenedVales }

        if (options.isEmpty()) return newScore // No valves left to turn

        return options.maxOf { tunnel ->
            bestRelease(
                currentId = tunnel.towardsId,
                timeRemaining = timeRemaining - 1 - tunnel.cost,
                scoreSoFar = newScore,
                unopenedVales = unopenedVales.apply { remove(currentId) }
            ).also {
                unopenedVales.add(currentId)
            }
        }
    }
}