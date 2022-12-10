package year2021.day17

import utils.Solution
import kotlin.math.max
import kotlin.math.sign

fun main() {
    with(Day17()) {
        testFile(::part1, 45)
        testFile(::part2, 112)
        solve()
    }
}

class Day17 : Solution() {
    override val day = 17
    override val year = 2021

    override fun part1(): Any {
        val targetX = input.first()
            .substringBefore(',')
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { it[0]..it[1] }

        val targetY = input.first()
            .substringAfter(',')
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { it[0]..it[1] }

        val maxYSpeedToCheck = 500
        val possibleYSpeeds = (0..maxYSpeedToCheck).filter { ySpeed ->
            val sim = Simulation(0, ySpeed)
            while (sim.y >= targetY.first) {
                if (sim.y in targetY) return@filter true
                sim.step()
            }
            return@filter false
        }

        val maxXSpeedToCheck = max(targetX.first, targetX.last) * targetX.first.sign
        val bestYSpeed = possibleYSpeeds.reversed().first { ySpeed ->
            (0..maxXSpeedToCheck).any { xSpeed ->
                val sim = Simulation(xSpeed, ySpeed)
                while (sim.y >= targetY.first) {
                    if (sim.y in targetY && sim.x in targetX) return@any true
                    sim.step()
                }
                return@any false
            }
        }

        return with(Simulation(0, bestYSpeed)) {
            while (currentYSpeed > 0) step()
            y
        }
    }

    override fun part2(): Any {
        val targetX = input.first()
            .substringBefore(',')
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { it[0]..it[1] }

        val targetY = input.first()
            .substringAfter(',')
            .substringAfter('=')
            .split("..")
            .map(String::toInt)
            .let { it[0]..it[1] }

        val maxYSpeedToCheck = 500
        val minYSpeedToCheck = -500
        val possibleYSpeeds = (minYSpeedToCheck..maxYSpeedToCheck).filter { ySpeed ->
            val sim = Simulation(0, ySpeed)
            while (sim.y >= targetY.first) {
                if (sim.y in targetY) return@filter true
                sim.step()
            }
            return@filter false
        }

        val maxXSpeedToCheck = max(targetX.first, targetX.last) * targetX.first.sign
        return possibleYSpeeds.sumOf { ySpeed ->
            (0..maxXSpeedToCheck).count { xSpeed ->
                val sim = Simulation(xSpeed, ySpeed)
                while (sim.y >= targetY.first) {
                    if (sim.y in targetY && sim.x in targetX) return@count true
                    sim.step()
                }
                return@count false
            }
        }
    }

    private class Simulation(initialXSpeed: Int, initialYSpeed: Int) {
        var currentXSpeed = initialXSpeed
        var currentYSpeed = initialYSpeed
        var x = 0
        var y = 0

        fun step() {
            x += currentXSpeed
            y += currentYSpeed

            currentXSpeed -= currentXSpeed.sign
            currentYSpeed -= 1
        }
    }
}