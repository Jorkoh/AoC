package year2015.day03

import utils.Solution

fun main() {
    with(Day03()) {
        test(::part1, 2, ">")
        test(::part1, 4, "^>v<")
        test(::part1, 2, "^v^v^v^v^v")

        test(::part2, 3, "^v")
        test(::part2, 3, "^>v<")
        test(::part2, 11, "^v^v^v^v^v")

        solve()
    }
}

private class Day03 : Solution {
    override val day = 3
    override val year = 2015

    override fun part1(input: List<String>): Any {
        var x = 0
        var y = 0
        val visited = mutableSetOf(x to y)

        for (c in input.first()) {
            when (c) {
                '^' -> y += 1
                '>' -> x += 1
                'v' -> y -= 1
                else -> x -= 1
            }
            visited.add(x to y)
        }

        return visited.size
    }

    override fun part2(input: List<String>): Any {
        var xSanta = 0
        var ySanta = 0
        var xRobo = 0
        var yRobo = 0
        val visited = mutableSetOf(xSanta to ySanta)

        for ((i, c) in input.first().withIndex()) {
            when (c) {
                '^' -> if(i % 2 == 0) ySanta += 1 else yRobo += 1
                '>' -> if(i % 2 == 0) xSanta += 1 else xRobo += 1
                'v' -> if(i % 2 == 0) ySanta -= 1 else yRobo -= 1
                else -> if(i % 2 == 0) xSanta -= 1 else xRobo -= 1
            }
            visited.add(if(i % 2 == 0) xSanta to ySanta else xRobo to yRobo)
        }

        return visited.size
    }
}