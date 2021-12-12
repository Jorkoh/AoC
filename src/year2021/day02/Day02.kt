package year2021.day02

import utils.Solution

fun main() {
    with(Day02()) {
        test(::part1, 150)
        test(::part2, 900)
        solve()
    }
}

private class Day02 : Solution {
    override val day = 2
    override val year = 2021

    override fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0

        input.forEach { line ->
            val amount = line.substringAfter(' ').toInt()
            when (line[0]) {
                'f' -> horizontal += amount
                'd' -> depth += amount
                'u' -> depth -= amount
            }
        }

        return horizontal * depth
    }

    override fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        input.forEach { line ->
            val amount = line.substringAfter(' ').toInt()
            when (line[0]) {
                'f' -> {
                    horizontal += amount
                    depth += aim * amount
                }
                'd' -> aim += amount
                'u' -> aim -= amount
            }
        }

        return horizontal * depth
    }
}