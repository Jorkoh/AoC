package year2020.day01

import utils.Solution

fun main() {
    with(Day01()) {
        test(::part1, 514579)
        test(::part2, 241861950)
        solve()
    }
}

private class Day01 : Solution {
    override val day = 1
    override val year = 2020

    override fun part1(input: List<String>): Int {
        val values = input.map { it.toInt() }.toSet()

        values.forEach { first ->
            val second = 2020 - first
            if (second in values) return first * second
        }

        throw IllegalStateException()
    }

    override fun part2(input: List<String>): Int {
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