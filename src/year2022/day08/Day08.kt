package year2022.day08

import utils.Solution
import year2022.day08.Day08.Direction.*

fun main() {
    with(Day08()) {
        testFile(::part1, 21)
        testFile(::part2, 8)
        solve()
        benchmark()
    }
}

private class Day08 : Solution {
    override val day = 8
    override val year = 2022

    enum class Direction { Left, Right, Top, Bottom }

    override fun part1(input: List<String>): Any {
        val fromLeft = input.isVisible(Left)
        val fromRight = input.isVisible(Right)
        val fromTop = input.isVisible(Top)
        val fromBottom = input.isVisible(Bottom)

        return input.indices.sumOf { i ->
            input.indices.count { j ->
                fromLeft[i][j] || fromRight[i][j] || fromTop[i][j] || fromBottom[i][j]
            }
        }
    }

    private fun List<String>.isVisible(from: Direction): List<List<Boolean>> {
        val result = MutableList(size) { MutableList(size) { false } }
        for (i in indices) {
            var max = -1
            for (j in indices) {
                val (r, c) = when (from) {
                    Left -> i to j
                    Right -> i to lastIndex - j
                    Top -> j to i
                    Bottom -> lastIndex - j to i
                }
                val h = this[r][c] - '0'
                result[r][c] = h > max
                if (h > max) max = h
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        val towardsLeft = input.visionRange(Left)
        val towardsRight = input.visionRange(Right)
        val towardsTop = input.visionRange(Top)
        val towardsBottom = input.visionRange(Bottom)

        return input.indices.maxOf { i ->
            input.indices.maxOf { j ->
                towardsLeft[i][j] * towardsRight[i][j] * towardsTop[i][j] * towardsBottom[i][j]
            }
        }
    }

    private fun List<String>.visionRange(towards: Direction): List<List<Int>> {
        val result = MutableList(size) { MutableList(size) { 0 } }
        for (i in indices) {
            val hToPos = IntArray(10) { 0 } // Relative to edge
            for (j in indices) {
                val (r, c) = when (towards) {
                    Left -> i to j
                    Right -> i to lastIndex - j
                    Top -> j to i
                    Bottom -> lastIndex - j to i
                }
                val h = this[r][c] - '0'
                val closestBlocking = (h..9).maxOf { hToPos[it] }
                result[r][c] = j - closestBlocking
                hToPos[h] = j
            }
        }
        return result
    }
}
