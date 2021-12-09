package year2021.day03

import printResults

fun main() {
    fun part1(input: List<String>): Int {
        val gamma = input.first().indices.map { i -> if (input.count { it[i] == '1' } >= input.size / 2f) '1' else '0' }
        val epsilon = gamma.map { if (it == '1') '0' else '1' }

        return gamma.bToD() * epsilon.bToD()
    }

    fun part2(input: List<String>): Int {
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

    printResults(::part1, ::part2, 198, 230, 3, 2021)
}

fun String.bToD(): Int = Integer.parseInt(this, 2)
fun List<Char>.bToD(): Int = joinToString("").bToD()
