package year2022.day20

import utils.Solution

fun main() {
    with(Day20()) {
        testFile(::part1, 3)
//        testFile(::part2, Unit)
        solve()
    }
}

class Day20 : Solution() {
    override val day = 20
    override val year = 2022

    private data class Instruction(val value: Int, var pos: Int)

    // Values are not unique so we need to keep track
    override fun part1(): Any {
        val allIns = input.mapIndexed { i, s -> Instruction(s.toInt(), i) }
        allIns.forEach { ins ->
            val oldPos = ins.pos
            val newPos = (ins.pos + ins.value).mod(allIns.size - 1)
            ins.pos = if (newPos == 0) allIns.lastIndex else newPos
            when {
                oldPos < newPos -> { // went up
                    // if it went up need to move everything between old and new position down
                    allIns.filter { it != ins && it.pos in oldPos..newPos }.forEach { i -> i.pos-- }
                }
                oldPos > newPos -> { // went down
                    // if it went down need to move everything between old and new position up
                    allIns.filter { it != ins && it.pos in newPos..oldPos }.forEach { i -> i.pos++ }
                }
            }
            println(allIns.sortedBy { it.pos }.joinToString { it.value.toString() })
        }
        val reference = allIns.first { it.value == 0 }.pos
        return allIns.first { it.pos == (reference + 1000) % allIns.size }.value +
                allIns.first { it.pos == (reference + 2000) % allIns.size }.value +
                allIns.first { it.pos == (reference + 3000) % allIns.size }.value
    }

    override fun part2(): Any {
        return Unit
    }
}