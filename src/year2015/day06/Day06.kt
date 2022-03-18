package year2015.day06

import utils.Solution
import kotlin.math.max

fun main() {
    with(Day06()) {
        testFile(::part1, 998996)
        testFile(::part2, 1001996)
        solve()
    }
}

private class Day06 : Solution {
    override val day = 6
    override val year = 2015

    override fun part1(input: List<String>): Any {
        val instructions = input.parseInstructions()
        val n = 1000

        return (0 until n).sumOf { y ->
            (0 until n).count { x ->
                var toggles = 0

                for (i in instructions.lastIndex downTo 0) {
                    val instruction = instructions[i]

                    // Skip if it doesn't affect this light
                    if(x !in instruction.x1..instruction.x2 || y !in instruction.y1..instruction.y2) continue

                    // Terminal instructions return, toggles keep counting
                    when(instruction.type){
                        InstructionType.ON -> return@count toggles % 2 == 0
                        InstructionType.OFF -> return@count toggles % 2 != 0
                        InstructionType.TOGGLE -> toggles += 1
                    }
                }

                // If we didn't encounter a terminal instruction it started unlit
                toggles % 2 != 0
            }
        }
    }

    override fun part2(input: List<String>): Any {
        val instructions = input.parseInstructions()
        val n = 1000

        return (0 until n).sumOf { y ->
            (0 until n).sumOf { x ->
                var brightness = 0

                instructions.forEach { instruction ->
                    // Skip if it doesn't affect this light
                    if(x !in instruction.x1..instruction.x2 || y !in instruction.y1..instruction.y2) return@forEach

                    when(instruction.type){
                        InstructionType.ON -> brightness += 1
                        InstructionType.OFF -> brightness = max(0, brightness - 1)
                        InstructionType.TOGGLE -> brightness += 2
                    }
                }

                brightness
            }
        }
    }

    private fun List<String>.parseInstructions() = map { line ->
        val (instructionString, firstCoords, secondCoords) = Regex(
            "(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)"
        ).find(line)!!.destructured

        Instruction(
            type = when (instructionString) {
                "turn on" -> InstructionType.ON
                "turn off" -> InstructionType.OFF
                else -> InstructionType.TOGGLE
            },
            x1 = firstCoords.split(',')[0].toInt(),
            y1 = firstCoords.split(',')[1].toInt(),
            x2 = secondCoords.split(',')[0].toInt(),
            y2 = secondCoords.split(',')[1].toInt(),
        )
    }

    private data class Instruction(val type: InstructionType, val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    private enum class InstructionType {
        ON, OFF, TOGGLE
    }
}