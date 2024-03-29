package year2020.day02

import utils.Solution

fun main() {
    with(Day02()) {
        testFile(::part1, 2)
        testFile(::part2, 1)
        solve()
    }
}

class Day02 : Solution() {
    override val day = 2
    override val year = 2020

    override fun part1(): Int {
        return input.count { line ->
            val (range, letter, password) = line.split(' ')
            val (min, max) = range.split('-').map { it.toInt() }

            password.count { it == letter[0] } in min..max
        }
    }

    override fun part2(): Int {
        return input.count { line ->
            val (positions, letter, password) = line.split(' ')
            val (first, second) = positions.split('-').map { it.toInt() - 1 }

            (password[first] == letter[0]) xor (password[second] == letter[0])
        }
    }
}