package year2021.day01

import printResults

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(2).count { it.last() > it.first() }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(4).count { it.last() > it.first() }
    }

    printResults(::part1, ::part2, 7, 5, 1, 2021)
}