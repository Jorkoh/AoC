package year2015.day11

import utils.Solution

fun main() {
    with(Day11()) {
        test(::part1, "abcdffaa", "abcdefgh")
        test(::part1, "ghjaabcc", "ghijklmn")
        solve()
    }
}

class Day11 : Solution() {
    override val day = 11
    override val year = 2015

    override fun part1(): Any {
        val password = Password(input.first())

        do {
            password.increase()
        } while (!password.isValid())

        return password.toString()
    }

    override fun part2(): Any {
        val password = Password(input.first())

        repeat(2) {
            do {
                password.increase()
            } while (!password.isValid())
        }

        return password.toString()
    }

    private class Password(initialString: String) {

        companion object {
            private val bannedCharCodes = listOf('i', 'o', 'l').map { c -> c.code - 'a'.code }.toSet()
        }

        private val values: IntArray

        init {
            values = initialString.map { c ->
                c.code - 'a'.code
            }.toIntArray()
        }

        fun increase() {
            for (i in values.lastIndex downTo 0) {
                if (values[i] == 25) {
                    // Carry
                    values[i] = 0
                } else {
                    values[i] += 1
                    break
                }
            }
        }

        fun isValid() = firstRule() && secondRule() && thirdRule()

        private fun firstRule() = (2..values.lastIndex).any { i ->
            values[i - 2] == values[i] - 2 && values[i - 1] == values[i] - 1
        }

        private fun secondRule() = values.none { it in bannedCharCodes }

        private fun thirdRule(): Boolean {
            val firstRepeat = (1..values.lastIndex).indexOfFirst { i -> values[i - 1] == values[i] }
            return if (firstRepeat == -1) {
                false
            } else {
                (1..values.lastIndex).any { i -> values[i] != values[firstRepeat] && values[i - 1] == values[i] }
            }
        }

        override fun toString() = values.map { code -> (code + 'a'.code).toChar() }.joinToString("")
    }
}