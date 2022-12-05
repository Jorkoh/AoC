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
        val stacks = input.parseStacks()
        input.dropWhile { it.firstOrNull() != 'm' }.map(::parseInstruction).forEach { (count, from, to) ->
            repeat(count) {
                val toMove = stacks[from - 1].removeLast()
                stacks[to - 1].addLast(toMove)
            }
        }

        return stacks.map { it.last() }.joinToString(separator = "")
    }

    override fun part2(input: List<String>): Any {
        val stacks = input.parseStacks()
        input.dropWhile { it.firstOrNull() != 'm' }.map(::parseInstruction).forEach { (count, from, to) ->
            val toMove = stacks[from - 1].takeLast(count)
            repeat(count) { stacks[from - 1].removeLast() }
            stacks[to - 1].addAll(toMove)
        }

        return stacks.map { it.last() }.joinToString(separator = "")
    }

    private fun List<String>.parseStacks(): List<ArrayDeque<Char>> {
        return List((first().length + 1) / 4) { ArrayDeque<Char>() }.also { stacks ->
            takeWhile { it[1] != '1' }.forEach { l ->
                stacks.forEachIndexed { i, stack ->
                    val c = l.getOrNull(i * 4 + 1) ?: ' '
                    if (c != ' ') stack.addFirst(c)
                }
            }
        }
    }

    private val regex = Regex("""\D+(\d+)\D+(\d+)\D+(\d+)""")
    private fun parseInstruction(line: String) = regex.find(line)!!.groupValues.drop(1).map(String::toInt)
}
