package year2015.day05

import utils.Solution

fun main() {
    with(Day05()) {
        test(::part1, 1, "ugknbfddgicrmopn")
        test(::part1, 1, "aaa")
        test(::part1, 0, "jchzalrnumimnmhp")
        test(::part1, 0, "haegwjzuvuyypxyu")
        test(::part1, 0, "dvszwmarrgswjxmb")
        test(::part2, 1, "qjhvhtzxzqqjkmpb")
        test(::part2, 1, "xxyxx")
        test(::part2, 0, "uurcxstgmygtbstg")
        test(::part2, 0, "ieodomkazucvgmuy")
        test(::part2, 0, "aaa")
        test(::part2, 1, "xxxx")
        solve()
    }
}

class Day05 : Solution() {
    override val day = 5
    override val year = 2015

    override fun part1(): Any {
        val vowels = setOf('a', 'e', 'i', 'o', 'u')
        val dangerous = setOf('a', 'c', 'p', 'x')

        return input.count { s ->
            var vowelCount = 0
            var hasRepeated = false

            for ((i, c) in s.withIndex()) {
                if (c in vowels) {
                    vowelCount += 1
                }

                if (i < s.lastIndex) {
                    val next = s[i + 1]
                    if (c == next) {
                        hasRepeated = true
                    } else if (c in dangerous && next.code == c.code + 1) {
                        return@count false
                    }
                }
            }

            vowelCount >= 3 && hasRepeated
        }
    }

    override fun part2(): Any {
        return input.count { s ->
            var previousPair: Pair<Char, Char>? = null
            val pairs = mutableSetOf<Pair<Char, Char>>()
            val hasNoValidPairs = s.zipWithNext().all { pair ->
                if (pair == previousPair) {
                    previousPair = null
                    true
                } else {
                    previousPair = pair
                    pairs.add(pair)
                }
            }
            if (hasNoValidPairs) return@count false

            for (i in 1 until s.lastIndex) {
                if (s[i - 1] == s[i + 1]) return@count true
            }

            // Pair but no sandwich
            false
        }
    }
}