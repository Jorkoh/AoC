package year2021.day13

import utils.Solution

fun main() {
    with(Day13()) {
        test(::part1, 17)
        solve()
    }
}

private class Day13 : Solution {
    override val day = 13
    override val year = 2021

    override fun part1(input: List<String>): Any {
        val initialPoints = input.takeWhile(String::isNotBlank).map { line ->
            line.split(',').map { it.toInt() }.let { it[0] to it[1] }
        }
        val instructions = input.drop(initialPoints.size + 1).map { line ->
            line.drop(11).split('=').let { it[0] to it[1].toInt() }
        }

        val points = instructions.take(1).fold(initialPoints) { points, (axis, value) ->
            points.map { (x, y) ->
                when {
                    axis == "y" && y > value -> x to value - (y - value)
                    axis == "x" && x > value -> value - (x - value) to y
                    else -> x to y
                }
            }.distinct()
        }

        return points.size
    }

    override fun part2(input: List<String>): Any {
        val initialPoints = input.takeWhile(String::isNotBlank).map { line ->
            line.split(',').map { it.toInt() }.let { it[0] to it[1] }
        }
        val instructions = input.drop(initialPoints.size + 1).map { line ->
            line.drop(11).split('=').let { it[0] to it[1].toInt() }
        }

        val points = instructions.fold(initialPoints) { points, (axis, value) ->
            points.map { (x, y) ->
                when {
                    axis == "y" && y > value -> x to value - (y - value)
                    axis == "x" && x > value -> value - (x - value) to y
                    else -> x to y
                }
            }.distinct()
        }

        return buildString {
            append('\n')
            for (y in 0..points.maxOf { it.second }) {
                for (x in 0..points.maxOf { it.first }) {
                    if (x to y in points) append('█')
                    else append(' ')
                }
                append('\n')
            }
        }
    }
}