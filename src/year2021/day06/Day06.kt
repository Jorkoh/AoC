package year2021.day06

import utils.Solution

fun main() {
    with(Day06()) {
        testFile(::part1, 5934L)
        testFile(::part2, 26984457539L)
        solve()
    }
}

private class Day06 : Solution {
    override val day = 6
    override val year = 2021

    override fun part1(input: List<String>): Long {
        var fish = LongArray(9)
        input.first().split(',').forEach { fish[it.toInt()]++ }

        repeat(80) {
            fish = LongArray(9) { i -> fish[(i + 1) % 9] + if (i == 6) fish[0] else 0 }
        }

        return fish.sum()
    }

    override fun part2(input: List<String>): Long {
        var fish = LongArray(9)
        input.first().split(',').forEach { fish[it.toInt()]++ }

        repeat(256) {
            fish = LongArray(9) { i -> fish[(i + 1) % 9] + if (i == 6) fish[0] else 0 }
        }

        return fish.sum()
    }
}
