package year2021.day03

import utils.Solution

fun main() {
    with(Day03()) {
        testFile(::part1, 198)
        testFile(::part2, 230)
        solve()
    }
}

class Day03 : Solution() {
    override val day = 3
    override val year = 2021

    override fun part1(): Int {
        val gamma = input.first().indices.map { i -> if (input.count { it[i] == '1' } >= input.size / 2f) '1' else '0' }
        val epsilon = gamma.map { if (it == '1') '0' else '1' }

        return gamma.bToD() * epsilon.bToD()
    }

    override fun part2(): Int {
        val remainingO2 = input.toMutableList()
        val remainingCO2 = input.toMutableList()

        input.first().indices.forEach { bit ->
            with(remainingO2) {
                val target = if (count { it[bit] == '1' } >= size / 2f) '1' else '0'
                removeIf { size > 1 && it[bit] != target }
            }

            with(remainingCO2) {
                val target = if (count { it[bit] == '1' } >= size / 2f) '0' else '1'
                removeIf { size > 1 && it[bit] != target }
            }
        }

        return remainingO2.first().bToD() * remainingCO2.first().bToD()
    }
}

private fun String.bToD(): Int = Integer.parseInt(this, 2)
private fun List<Char>.bToD(): Int = joinToString("").bToD()
