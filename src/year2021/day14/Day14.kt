package year2021.day14

import utils.Solution

fun main() {
    with(Day14()) {
        testFile(::part1, 1588L)
        testFile(::part2, 2188189693529L)
        solve()
    }
}

class Day14 : Solution() {
    override val day = 14
    override val year = 2021

    override fun part1(): Any {
        var pairCounts = input.first().windowed(2).associate { (it[0] to it[1]) to 1L }
        val rules = input.drop(2).associate { (it[0] to it[1]) to it[6] }

        repeat(10) {
            pairCounts = buildMap {
                pairCounts.forEach { (pair, count) ->
                    merge(pair.first to rules[pair]!!, count, Long::plus)
                    merge(rules[pair]!! to pair.second, count, Long::plus)
                }
            }
        }

        val charCounts = buildMap<Char, Long> {
            pairCounts.forEach { (first, second), count ->
                merge(first, count, Long::plus)
                merge(second, count, Long::plus)
            }
            merge(input.first().first(), 1, Long::plus)
            merge(input.first().last(), 1, Long::plus)
        }

        return charCounts.toList().sortedBy { it.second }.let { it.last().second - it.first().second } / 2
    }

    override fun part2(): Any {
        var pairCounts = input.first().windowed(2).associate { (it[0] to it[1]) to 1L }
        val rules = input.drop(2).associate { (it[0] to it[1]) to it[6] }

        repeat(40) {
            pairCounts = buildMap {
                pairCounts.forEach { (pair, count) ->
                    merge(pair.first to rules[pair]!!, count, Long::plus)
                    merge(rules[pair]!! to pair.second, count, Long::plus)
                }
            }
        }

        val charCounts = buildMap<Char, Long> {
            pairCounts.forEach { (first, second), count ->
                merge(first, count, Long::plus)
                merge(second, count, Long::plus)
            }
            merge(input.first().first(), 1, Long::plus)
            merge(input.first().last(), 1, Long::plus)
        }

        return charCounts.toList().sortedBy { it.second }.let { it.last().second - it.first().second } / 2
    }
}