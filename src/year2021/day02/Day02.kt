package year2021.day02

import printResults

fun main() {
    fun part1(input: List<String>): Int {
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

    fun part2(input: List<String>): Int {
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

    printResults(::part1, ::part2, 150, 900, 2, 2021)
}