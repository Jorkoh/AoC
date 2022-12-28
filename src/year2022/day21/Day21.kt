package year2022.day21

import utils.Solution

fun main() {
    with(Day21()) {
        testFile(::part1, 152L)
        solve()
    }
}

class Day21 : Solution() {
    override val day = 21
    override val year = 2022

    override fun part1(): Any {
        val monkeys = input.associate { it.take(4) to it.drop(6) }
        return monkeys.eval("root")
    }

    private fun Map<String, String>.eval(id: String): Long {
        val ins = get(id)!!
        return if (ins.first().isDigit()) {
            ins.toLong()
        } else {
            val (idA, op, idB) = ins.split(' ')
            when (op) {
                "+" -> eval(idA) + eval(idB)
                "-" -> eval(idA) - eval(idB)
                "*" -> eval(idA) * eval(idB)
                "/" -> eval(idA) / eval(idB)
                else -> throw IllegalStateException()
            }
        }
    }

    override fun part2(): Any {
        return Unit
    }
}
