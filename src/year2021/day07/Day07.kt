package year2021.day07

import utils.Solution
import kotlin.math.abs

fun main() {
    with(Day07()) {
        testFile(::part1, 37)
        testFile(::part2, 168)
        solve()
    }
}

private class Day07 : Solution {
    override val day = 7
    override val year = 2021

    override fun part1(input: List<String>): Int {
        val positions = input.first().split(',').map { it.toInt() }.sorted()

        // Optimally use the median as target
        return (positions.first()..positions.last()).minOf { target ->
            positions.sumOf { abs(target - it) }
        }
    }

    override fun part2(input: List<String>): Int {
        val positions = input.first().split(',').map { it.toInt() }.sorted()

        // Optimally use the mean, mean-1 and mean +1 as targets
        return (positions.first()..positions.last()).minOf { target ->
            positions.sumOf { abs(target - it).let { d -> 0.5 * d * (d + 1) }.toInt() }
        }
    }
}
