package year2021.day06

import printResults

fun main() {
    fun part1(input: List<String>): Long {
        var fish = LongArray(9)
        input.first().split(',').forEach { fish[it.toInt()]++ }

        repeat(80) {
            fish = LongArray(9) { i -> fish[(i + 1) % 9] + if (i == 6) fish[0] else 0 }
        }

        return fish.sum()
    }

    fun part2(input: List<String>): Long {
        var fish = LongArray(9)
        input.first().split(',').forEach { fish[it.toInt()]++ }

        repeat(256) {
            fish = LongArray(9) { i -> fish[(i + 1) % 9] + if (i == 6) fish[0] else 0 }
        }

        return fish.sum()
    }

    printResults(::part1, ::part2, 5934L, 26984457539L, 6, 2021)
}
