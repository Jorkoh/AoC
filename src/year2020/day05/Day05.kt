package year2020.day05

import utils.Solution

fun main() {
    with(Day05()) {
        testFile(::part1, 357)
        solve()
    }
}

class Day05 : Solution() {
    override val day = 5
    override val year = 2020

    override fun part1(): Any {
        return input.maxOf { pass ->
            pass.map { if (it in listOf('B', 'R')) '1' else '0' }.joinToString("").toInt(2)
        }
    }

    override fun part2(): Any {
        return input.map { pass ->
            pass.map { if (it in listOf('B', 'R')) '1' else '0' }.joinToString("").toInt(2)
        }.sorted().zipWithNext { a, b -> if (b != a + 1) return a + 1 }
    }
}