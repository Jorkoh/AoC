package year2020.day01

import utils.Solution

fun main() {
    with(Day01()) {
        testFile(::part1, 514579)
        testFile(::part2, 241861950)
        solve()
    }
}

class Day01 : Solution() {
    override val day = 1
    override val year = 2020

    override fun part1(): Int {
        val values = input.map { it.toInt() }.toSet()

        values.forEach { first ->
            val second = 2020 - first
            if (second in values) return first * second
        }

        throw IllegalStateException()
    }

    override fun part2(): Int {
        val values = input.map { it.toInt() }.toSet()

        values.forEach { first ->
            values.forEach { second ->
                val third = 2020 - first - second
                if (third in values) return first * second * third
            }
        }

        throw IllegalStateException()
    }
}