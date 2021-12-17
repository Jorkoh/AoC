package year2021.day17

import utils.Solution
import kotlin.math.max
import kotlin.math.sign

fun main() {
    with(Day17()){
        test(::part1, 45)
        solve()
    }
}

private class Day17 : Solution {
    override val day = 17
    override val year = 2021

    override fun part1(input: List<String>): Any {
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
            with(Simulation(0, ySpeed)) {
                while (y >= targetY.first) {
                    if (y in targetY) return@filter true
                    step()
                }
                return@filter false
            }
        }

        val bestCombo = possibleYSpeeds.reversed().firstNotNullOf { ySpeed ->
            val workingXSpeed = (0..max(targetX.first, targetX.last) * targetX.first.sign).firstOrNull { xSpeed ->
                with(Simulation(xSpeed, ySpeed)) {
                    while (y >= targetY.first) {
                        if (y in targetY && x in targetX) return@firstOrNull true
                        step()
                    }
                    return@firstOrNull false
                }
            }

            if (workingXSpeed == null) null
            else workingXSpeed to ySpeed
        }

        return with(Simulation(bestCombo.first, bestCombo.second)) {
            while (currentYSpeed > 0) step()
            y
        }
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
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