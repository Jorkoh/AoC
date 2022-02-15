package year2021.day23

import utils.Solution
import java.util.*

fun main() {
    with(Day23()) {
        testFile(::part1, 12521)
        testFile(::part2, 44169)
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
// NOTICING THAT IF AN AMPHI CAN ENTER ITS ROOM THAT'S THE BEST MOVE MADE IT GO FROM 55 TO 3 SECONDS
private class Day23 : Solution {
    override val day = 23
    override val year = 2021

    private enum class Type {
        A, B, C, D
    }

    private data class Amphi(val type: Type, val position: Int, val isResting: Boolean = false)

    private data class Board(val amphis: List<Amphi>, var score: Int = 0) {

        companion object {
            val memo = mutableMapOf<List<Amphi>, List<Pair<List<Amphi>, Int>>>()
            val stoppableHallwayIndices = listOf(0, 1, 3, 5, 7, 9, 10)
            val typeToRoomI = mapOf(
                Type.A to 11..14,
                Type.B to 15..18,
                Type.C to 19..22,
                Type.D to 23..26
            )
            val roomIToType = mapOf(
                11 to Type.A, 12 to Type.A, 13 to Type.A, 14 to Type.A,
                15 to Type.B, 16 to Type.B, 17 to Type.B, 18 to Type.B,
                19 to Type.C, 20 to Type.C, 21 to Type.C, 22 to Type.C,
                23 to Type.D, 24 to Type.D, 25 to Type.D, 26 to Type.D
            )
        }

        fun isWon() = amphis.all { amphis -> roomIToType[amphis.position] == amphis.type }

        fun nextBoards(score: Int, deep: Boolean): List<Board> = memo.computeIfAbsent(this.amphis) { initialAmphis ->
            val enterRoomCase = initialAmphis.filter { it.position.isHallway() }.firstNotNullOfOrNull { amphi ->
                val initialPos = amphi.position
                val roomIndices = if (deep) typeToRoomI[amphi.type]!! else typeToRoomI[amphi.type]!!.take(2)
                val roomAvailable = roomIndices.all {
                    val amphiAtRoom = getAt(it)
                    amphiAtRoom == null || amphiAtRoom.type == amphi.type
                }
                val rI = amphi.type.roomTypeToHallwayIndex()
                val steps = if (rI < initialPos) rI until initialPos else (initialPos + 1)..rI
                if (roomAvailable && steps.all { getAt(it) == null }) {
                    // Available and no obstructions
                    val roomStart = roomIndices.first()
                    val restingPosition = roomIndices.last { getAt(it) == null }
                    val multiplier = amphi.type.toMultiplier()
                    val toEnterRoom = restingPosition - roomStart + 1
                    val toTraverseHallway = (steps.last - steps.first) + 1
                    val cost = (toEnterRoom + toTraverseHallway) * multiplier
                    val newAmphis = initialAmphis
                        .filterNot { it == amphi }
                        .toMutableList().apply { add(amphi.copy(position = restingPosition, isResting = true)) }

                    newAmphis to cost
                } else {
                    null
                }
            }

            if (enterRoomCase != null) {
                listOf(enterRoomCase)
            } else {
                // exit room cases
                amphis.filter { amphi ->
                    !amphi.isResting && amphi.position.isRoom() && (typeToRoomI[roomIToType[amphi.position]]!!.first
                            until amphi.position).all { getAt(it) == null }
                }.flatMap { amphi ->
                    val initialPos = amphi.position
                    val roomStart = typeToRoomI[roomIToType[amphi.position]]!!.first
                    // Add all the possible hallway values
                    val rI = roomIToType[initialPos]!!.roomTypeToHallwayIndex()
                    stoppableHallwayIndices.mapNotNull { hI ->
                        val steps = if (rI < hI) rI..hI else hI..rI
                        if (steps.all { getAt(it) == null }) {
                            // No obstructions
                            val multiplier = amphi.type.toMultiplier()
                            val toExitRoom = initialPos - roomStart + 1
                            val toTraverseHallway = steps.last - steps.first
                            val cost = (toExitRoom + toTraverseHallway) * multiplier
                            val newAmphis = initialAmphis
                                .filterNot { it == amphi }
                                .toMutableList().apply { add(amphi.copy(position = hI)) }

                            newAmphis to cost
                        } else null
                    }
                }
            }
        }.map { (newAmphis, cost) -> Board(newAmphis, score + cost) }

        private fun Int.isHallway() = this <= 10
        private fun Int.isRoom() = this > 10

        private fun Type.toMultiplier() = when (this) {
            Type.A -> 1
            Type.B -> 10
            Type.C -> 100
            Type.D -> 1000
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
                Amphi(Type.valueOf(input[2][3].toString()), 11),
                Amphi(Type.valueOf(input[5][3].toString()), 12),

                Amphi(Type.valueOf(input[2][5].toString()), 15),
                Amphi(Type.valueOf(input[5][5].toString()), 16),

                Amphi(Type.valueOf(input[2][7].toString()), 19),
                Amphi(Type.valueOf(input[5][7].toString()), 20),

                Amphi(Type.valueOf(input[2][9].toString()), 23),
                Amphi(Type.valueOf(input[5][9].toString()), 24),
            ),
            score = 0
        )

        // BFS traverse
        val queue = ArrayDeque<Board>().apply { add(initialBoard) }
        val explored = mutableSetOf(initialBoard)
        var minimumScore = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val board = queue.removeFirst()
            when {
                board.score > minimumScore -> continue
                board.isWon() -> if (board.score < minimumScore) minimumScore = board.score
                else -> board.nextBoards(board.score, false).forEach { nextBoard ->
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
        val initialBoard = Board(
            amphis = listOf(
                Amphi(Type.valueOf(input[2][3].toString()), 11),
                Amphi(Type.valueOf(input[3][3].toString()), 12),
                Amphi(Type.valueOf(input[4][3].toString()), 13),
                Amphi(Type.valueOf(input[5][3].toString()), 14),

                Amphi(Type.valueOf(input[2][5].toString()), 15),
                Amphi(Type.valueOf(input[3][5].toString()), 16),
                Amphi(Type.valueOf(input[4][5].toString()), 17),
                Amphi(Type.valueOf(input[5][5].toString()), 18),

                Amphi(Type.valueOf(input[2][7].toString()), 19),
                Amphi(Type.valueOf(input[3][7].toString()), 20),
                Amphi(Type.valueOf(input[4][7].toString()), 21),
                Amphi(Type.valueOf(input[5][7].toString()), 22),

                Amphi(Type.valueOf(input[2][9].toString()), 23),
                Amphi(Type.valueOf(input[3][9].toString()), 24),
                Amphi(Type.valueOf(input[4][9].toString()), 25),
                Amphi(Type.valueOf(input[5][9].toString()), 26),
            ),
            score = 0
        )

        // BFS traverse
        val queue = ArrayDeque<Board>().apply { add(initialBoard) }
        val explored = mutableSetOf(initialBoard)
        var minimumScore = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val board = queue.removeFirst()
            when {
                board.score > minimumScore -> continue
                board.isWon() -> if (board.score < minimumScore) minimumScore = board.score
                else -> board.nextBoards(board.score, true).forEach { nextBoard ->
                    if (nextBoard !in explored) {
                        explored.add(nextBoard)
                        queue.addLast(nextBoard)
                    }
                }
            }
        }

        return minimumScore
    }
}