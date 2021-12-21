package year2021.day21

import utils.Solution
import java.lang.Long.max

fun main() {
    with(Day21()) {
        test(::part1, 739785)
        test(::part2, 444356092776315L)
        solve()
    }
}

private class Day21 : Solution {
    override val day = 21
    override val year = 2021

    override fun part1(input: List<String>): Any {
        var position1 = input[0].substringAfter("position: ").toInt()
        var position2 = input[1].substringAfter("position: ").toInt()
        var score1 = 0
        var score2 = 0
        var turn = 0
        var diePosition = 1

        while (score1 < 1000 && score2 < 1000) {
            val score = when (diePosition) {
                99 -> 200
                100 -> 103
                else -> diePosition * 3 + 1 + 2
            }

            if (turn % 2 == 0) {
                position1 += score
                if (position1 > 10) position1 %= 10
                if (position1 == 0) position1 = 10
                score1 += position1
            } else {
                position2 += score
                if (position2 > 10) position2 %= 10
                if (position2 == 0) position2 = 10
                score2 += position2
            }

            diePosition += 3
            if (diePosition > 100) diePosition %= 100
            turn++
        }

        return (if (turn % 2 == 0) score1 else score2) * (turn * 3)
    }

    data class BoardState(val score1: Int, val score2: Int, val position1: Int, val position2: Int, val turn: Int)

    override fun part2(input: List<String>): Any {
        val rollsToFrequency = listOf(3 to 1L, 4 to 3L, 5 to 6L, 6 to 7L, 7 to 6L, 8 to 3L, 9 to 1L)
        var wins1 = 0L
        var wins2 = 0L
        val initialState = BoardState(
            score1 = 0,
            score2 = 0,
            position1 = input[0].substringAfter("position: ").toInt(),
            position2 = input[1].substringAfter("position: ").toInt(),
            turn = 0
        )
        val boardStates = mutableMapOf(initialState to 1L)

        while (boardStates.isNotEmpty()) {
            val (boardState, count) = boardStates.entries.first().toPair()
            boardStates.remove(boardState)

            rollsToFrequency.forEach { (roll, frequency) ->
                var score1 = boardState.score1
                var score2 = boardState.score2
                var position1 = boardState.position1
                var position2 = boardState.position2

                if (boardState.turn % 2 == 0) {
                    position1 += roll
                    if (position1 > 10) position1 %= 10
                    if (position1 == 0) position1 = 10
                    score1 += position1
                } else {
                    position2 += roll
                    if (position2 > 10) position2 %= 10
                    if (position2 == 0) position2 = 10
                    score2 += position2
                }

                when {
                    score1 >= 21 -> wins1 += frequency * count
                    score2 >= 21 -> wins2 += frequency * count
                    else -> {
                        val newState = BoardState(score1, score2, position1, position2, boardState.turn + 1)
                        boardStates.merge(newState, frequency * count) { old, _ ->
                            old + frequency * count
                        }
                    }
                }
            }
        }

        return max(wins1, wins2)
    }
}