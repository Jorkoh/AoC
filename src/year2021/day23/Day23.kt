package year2021.day23

import utils.Solution
import java.util.*

fun main() {
    with(Day23()) {
        test(::part1, 12521)
        test(::part2, 44169)
        solve()
    }
}

// No diagonal moves
// 2 hallway positions on each side and 1 between each room for a total of 7
// A = 1, B = 10, C = 100 and D = 1000 (energy cost)
// RULES
//  1. Never stop on the space above a room
//  2. Once on the hallway can only move into a room if it's its final room and it's empty or with already placed
//  3. Once it stops moving on hallway can't move except to enter its final room
// To sum up: if they are in their starting spot they can move to available hallway, optionally stop and
// then enter their final room to rest (without blocking an external one in that room)
private class Day23 : Solution {
    override val day = 23
    override val year = 2021

    private enum class Type {
        A, B, C, D
    }

    private data class Amphi(val type: Type, val position: Int, val isResting: Boolean)

    private data class Board(val amphis: List<Amphi>, var score: Int = 0) {

        companion object {
            val memo = mutableMapOf<List<Amphi>, List<Pair<List<Amphi>, Int>>>()
            val stoppableHallwayIndices = listOf(0, 1, 3, 5, 7, 9, 10)
        }

        fun isWon() = amphis.all { amphis -> amphis.position.toRoomType() == amphis.type }

        fun nextBoards(score: Int, roomLength: Int): List<Board> = memo.computeIfAbsent(this.amphis) { initialAmphis ->
            buildList {
                // Only move those that are not in their final positions
                amphis.filterNot { it.isResting }.forEach { amphi ->
                    val initialPos = amphi.position
                    if (initialPos.isRoom()) {
                        // Early return if room exit is blocked // TODO adapt this for other depths
                        if (initialPos % 2 == 0 && getAt(initialPos - 1) != null) return@forEach
                        // Add all the possible hallway values
                        val rI = initialPos.toRoomType()!!.roomTypeToHallwayIndex()
                        stoppableHallwayIndices.forEach { hI ->
                            val steps = if (rI < hI) rI..hI else hI..rI
                            if (steps.all { getAt(it) == null }) {
                                // No obstructions, add it
                                val multiplier = amphi.type.toMultiplier()
                                val toExitRoom = if (initialPos % 2 == 0) 2 else 1 // TODO adapt
                                val toTraverseHallway = steps.last - steps.first
                                val cost = (toExitRoom + toTraverseHallway) * multiplier
                                val newAmphis = initialAmphis
                                    .filterNot { it == amphi }
                                    .toMutableList().apply { add(amphi.copy(position = hI)) }

                                add(newAmphis to cost)
                            }
                        }
                    } else {
                        // Add the move to room if it's possible
                        val roomIndices = amphi.type.toTargetRoomIndices()
                        val roomAvailable = roomIndices.map { getAt(it) }.all { it == null || it.type == amphi.type }
                        val bottomAvailable = getAt(roomIndices.first + roomLength - 1) == null

                        val rI = amphi.type.roomTypeToHallwayIndex()
                        val steps = if (rI < initialPos) rI until initialPos else (initialPos + 1)..rI
                        if (roomAvailable && steps.all { getAt(it) == null }) {
                            // Available and no obstructions, add it // TODO adapt
                            val restingPosition = if (bottomAvailable) roomIndices.first + roomLength - 1 else roomIndices.first
                            val multiplier = amphi.type.toMultiplier()
                            val toEnterRoom = if (bottomAvailable) 2 else 1 // TODO adapt
                            val toTraverseHallway = (steps.last - steps.first) + 1
                            val cost = (toEnterRoom + toTraverseHallway) * multiplier
                            val newAmphis = initialAmphis
                                .filterNot { it == amphi }
                                .toMutableList().apply { add(amphi.copy(position = restingPosition, isResting = true)) }

                            add(newAmphis to cost)
                        }
                    }
                }
            }
        }.map { (newAmphis, cost) -> Board(newAmphis, score + cost) }

        private fun Int.isRoom() = this > 10

        private fun Type.toMultiplier() = when (this) {
            Type.A -> 1
            Type.B -> 10
            Type.C -> 100
            Type.D -> 1000
        }

        private fun Int.toRoomType() = when (this) {
            11, 12, 13, 14 -> Type.A
            15, 16, 17, 18 -> Type.B
            19, 20, 21, 22 -> Type.C
            23, 24, 25, 26 -> Type.D
            else -> null
        }

        private fun Type.toTargetRoomIndices() = when (this) {
            Type.A -> 11..14
            Type.B -> 15..18
            Type.C -> 19..22
            Type.D -> 23..26
        }

        private fun Type.roomTypeToHallwayIndex() = when (this) {
            Type.A -> 2
            Type.B -> 4
            Type.C -> 6
            Type.D -> 8
        }

        private fun getAt(position: Int) = amphis.firstOrNull { it.position == position }
        private fun getAtChar(position: Int) = getAt(position)?.type?.name ?: "."

        override fun toString() = """
#############
#${(0..10).joinToString("") { getAtChar(it) }}#
###${getAtChar(11)}#${getAtChar(15)}#${getAtChar(19)}#${getAtChar(23)}###
  #${getAtChar(12)}#${getAtChar(16)}#${getAtChar(20)}#${getAtChar(24)}#
  #${getAtChar(13)}#${getAtChar(17)}#${getAtChar(21)}#${getAtChar(25)}#
  #${getAtChar(14)}#${getAtChar(18)}#${getAtChar(22)}#${getAtChar(26)}#
  #########
  """.trimIndent()
    }

    override fun part1(input: List<String>): Any {
        val initialBoard = Board(
            amphis = listOf(
                Amphi(Type.valueOf(input[2][3].toString()), 11, false),
                Amphi(Type.valueOf(input[5][3].toString()), 12, input[5][3] == 'A'),

                Amphi(Type.valueOf(input[2][5].toString()), 15, false),
                Amphi(Type.valueOf(input[5][5].toString()), 16, input[5][5] == 'B'),

                Amphi(Type.valueOf(input[2][7].toString()), 19, false),
                Amphi(Type.valueOf(input[5][7].toString()), 20, input[5][7] == 'C'),

                Amphi(Type.valueOf(input[2][9].toString()), 23, false),
                Amphi(Type.valueOf(input[5][9].toString()), 24, input[5][9] == 'D'),
            ),
            score = 0
        )

        val queue = ArrayDeque<Board>().apply { add(initialBoard) }
        val explored = mutableSetOf(initialBoard)
        var minimumScore = Int.MAX_VALUE

        while (queue.isNotEmpty()) {
            val board = queue.removeFirst()
            when {
                board.score > minimumScore -> continue
                board.isWon() -> if (board.score < minimumScore) minimumScore = board.score
                else -> board.nextBoards(board.score, 2).forEach { nextBoard ->
                    if (nextBoard !in explored) {
                        explored.add(nextBoard)
                        queue.addLast(nextBoard)
                    }
                }
            }
        }

        return minimumScore
    }

    override fun part2(input: List<String>): Any {

        return 0
    }
}