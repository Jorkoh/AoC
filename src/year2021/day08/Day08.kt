package year2021.day08

import utils.Solution
import kotlin.math.pow

fun main() {
    with(Day08()) {
        test(::part1, 26)
        test(::part2, 61229)
        solve()
    }
}

private class Day08 : Solution {
    override val day = 8
    override val year = 2021

    override fun part1(input: List<String>): Int {
        val entries = input.map { line ->
            val (signals, output) = line.split(" | ").map { it.split(' ') }
            Pair(signals, output)
        }

        return entries.sumOf { (_, output) -> output.count { it.length in setOf(2, 4, 3, 7) } }
    }

    override fun part2(input: List<String>): Int {
        val entries = input.map { line ->
            val (signals, output) = line.split(" | ").map { it.split(' ') }
            Pair(signals, output)
        }

        val decodedNumbers = entries.map { (signals, output) ->
            val digits = Array(10) { "" }

            // Some digits have unique segment count
            digits[1] = signals.first { it.length == 2 }
            digits[4] = signals.first { it.length == 4 }
            digits[7] = signals.first { it.length == 3 }
            digits[8] = signals.first { it.length == 7 }
            // Some segments appear a unique number of times
            val frequencies = signals.joinToString("").groupingBy { it }.eachCount()
            val topL = frequencies.entries.first { it.value == 6 }.key
            val bottomL = frequencies.entries.first { it.value == 4 }.key
            // Middle segment can now be deduced
            val middle = digits[4].first { it != topL && it !in digits[1] }
            // The other digits can now be deduced
            digits[0] = signals.first { it.length == 6 && middle !in it }
            digits[9] = signals.first { it.length == 6 && bottomL !in it }
            digits[6] = signals.first { it.length == 6 && it != digits[0] && it != digits[9] }
            digits[2] = signals.first { it.length == 5 && bottomL in it }
            digits[5] = signals.first { it.length == 5 && topL in it }
            digits[3] = signals.first { it.length == 5 && it != digits[2] && it != digits[5] }

            output.reversed().withIndex().sumOf { (i, outDigit) ->
                digits.indexOfFirst { digit ->
                    outDigit.length == digit.length && outDigit.all { it in digit }
                } * 10f.pow(i).toInt()
            }
        }

        return decodedNumbers.sum()
    }
}
