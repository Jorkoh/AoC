package year2022.day05

import utils.Solution

fun main() {
    with(Day05()) {
        testFile(::part1, "CMZ")
        testFile(::part2, "MCD")
        solve()
    }
}

private class Day05 : Solution {
    override val day = 5
    override val year = 2022

    override fun part1(input: List<String>): Any {
        val stacks = parseStacks(input)
        input.dropWhile { it.firstOrNull() != 'm' }.map(::parse).forEach { (count, from, to) ->
            repeat(count) {
                val removed = stacks[from - 1].removeLast()
                stacks[to - 1].addLast(removed)
            }
        }

        return stacks.joinToString(separator = "") { it.last().toString() }
    }

    override fun part2(input: List<String>): Any {
        val stacks = parseStacks(input)
        input.dropWhile { it.firstOrNull() != 'm' }.map(::parse).forEach { (count, from, to) ->
            val removed = stacks[from - 1].takeLast(count)
            repeat(count) { stacks[from - 1].removeLast() }
            stacks[to - 1].addAll(removed)
        }

        return stacks.joinToString(separator = "") { it.last().toString() }
    }

    private fun parseStacks(input: List<String>): List<ArrayDeque<Char>> {
        val rowCount = input.indexOfFirst { it.isBlank() } - 1
        val stacks = List((input[rowCount - 1].length + 1) / 4) { ArrayDeque<Char>() }

        input.take(rowCount).forEach { l ->
            stacks.forEachIndexed { i, stack ->
                val c = l.getOrNull(i * 4 + 1) ?: ' '
                if (c != ' ') stack.addFirst(c)
            }
        }

        return stacks
    }

    private val regex = Regex("""\D+(\d+)\D+(\d+)\D+(\d+)""")
    private fun parse(line: String) = regex.find(line)!!.groupValues.drop(1).map(String::toInt)
}
