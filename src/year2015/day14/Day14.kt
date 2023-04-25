package year2015.day14

import utils.Solution
import year2015.day14.Day14.Reindeer.Companion.toReindeer

fun main() {
    with(Day14()) {
        solve()
    }
}

class Day14 : Solution() {
    override val day = 14
    override val year = 2015

    override fun part1(): Any {
        val racers = input.map { it.toReindeer() }
        val seconds = 2503

        return racers.maxOf { racer ->
            val distanceFullCycles = racer.speed * racer.runTime * (seconds / racer.cycleTime)
            val distanceLastCycle = racer.speed * (seconds % racer.cycleTime).coerceAtMost(racer.runTime)

            distanceFullCycles + distanceLastCycle
        }
    }

    override fun part2(): Any {
        val racers = input.map { TrackedReindeer(it.toReindeer()) }
        val seconds = 2503

        repeat(seconds) { second ->
            racers.forEach { racer ->
                val lastCycleTime = second % racer.reindeer.cycleTime
                if (lastCycleTime < racer.reindeer.runTime) {
                    // if it's currently running update its position
                    racer.position += racer.reindeer.speed
                }
            }

            // award points to the current leader, if they are tied for the lead give one point to each of them
            val lead = racers.maxOf { it.position }
            racers.filter { it.position == lead }.forEach { it.points += 1 }
        }

        return racers.maxOf { it.points }
    }

    data class Reindeer(val name: String, val speed: Int, val runTime: Int, val restTime: Int) {
        companion object {
            fun String.toReindeer(): Reindeer {
                val parts = split(" ")

                return Reindeer(
                    name = parts[0],
                    speed = parts[3].toInt(),
                    runTime = parts[6].toInt(),
                    restTime = parts[13].toInt()
                )
            }
        }

        val cycleTime = runTime + restTime
    }

    data class TrackedReindeer(val reindeer: Reindeer, var position: Int = 0, var points: Int = 0)
}