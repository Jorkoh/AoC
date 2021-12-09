package year2021.day07

import printResults
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input.first().split(',').map { it.toInt() }.sorted()

        // Optimally use the median as target
        return (positions.first()..positions.last()).minOf { target ->
            positions.sumOf { abs(target - it) }
        }
    }

    fun part2(input: List<String>): Int {
        val positions = input.first().split(',').map { it.toInt() }.sorted()

        // Optimally use the mean, mean-1 and mean +1 as targets
        return (positions.first()..positions.last()).minOf { target ->
            positions.sumOf { abs(target - it).let { d -> 0.5 * d * (d + 1) }.toInt() }
        }
    }

    printResults(::part1, ::part2, 37, 168, 7, 2021)
}
