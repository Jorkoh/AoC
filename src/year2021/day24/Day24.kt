package year2021.day24

import utils.Solution

fun main() {
    with(Day24()) {
        solve()
    }
}

private class Day24 : Solution {
    override val day = 24
    override val year = 2021

    // Stolen from https://www.reddit.com/r/adventofcode/comments/rnejv5//hpsrc8j/
    private fun common(input: List<String>, part2: Boolean): Long {
        val blocks = input.chunked(18)
        val result = MutableList(14) { -1 }
        val buffer = ArrayDeque<Pair<Int, Int>>()
        fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
        val best = if (part2) 1 else 9
        blocks.forEachIndexed { index, instructions ->
            if ("div z 26" in instructions) {
                val offset = instructions.lastOf("add x")
                val (lastIndex, lastOffset) = buffer.removeFirst()
                val difference = offset + lastOffset
                if (difference >= 0) {
                    result[lastIndex] = if (part2) best else best - difference
                    result[index] = if (part2) best + difference else best
                } else {
                    result[lastIndex] = if (part2) best - difference else best
                    result[index] = if (part2) best else best + difference
                }
            } else buffer.addFirst(index to instructions.lastOf("add y"))
        }

        return result.joinToString("").toLong()
    }

    override fun part1(input: List<String>): Any {
        return common(input, false)
    }

    override fun part2(input: List<String>): Any {
        return common(input, true)
    }
}