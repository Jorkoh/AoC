package year2022.day17

import utils.Solution
import kotlin.math.max

fun main() {
    with(Day17()) {
        testFile(::part1, 3068)
        testFile(::part2, 1514285714288L)
        solve()
    }
}

class Day17 : Solution() {
    override val day = 17
    override val year = 2022

    private data class Co(val x: Int, val y: Int)
    private data class Shape(val coords: List<Co>, val low: Int)

    private operator fun Co.plus(b: Co) = Co(x + b.x, y + b.y)
    private val shapes = listOf(
        Shape(listOf(Co(0, 0), Co(1, 0), Co(2, 0), Co(3, 0)), 0),
        Shape(listOf(Co(1, 0), Co(0, -1), Co(1, -1), Co(2, -1), Co(1, -2)), 2),
        Shape(listOf(Co(2, 0), Co(2, -1), Co(0, -2), Co(1, -2), Co(2, -2)), 2),
        Shape(listOf(Co(0, 0), Co(0, -1), Co(0, -2), Co(0, -3)), 3),
        Shape(listOf(Co(0, 0), Co(1, 0), Co(0, -1), Co(1, -1)), 1)
    )

    override fun part1(): Any {
        val wind = input.first()
        var turn = 0
        val filled = mutableSetOf<Co>()
        var shapeCount = 0
        var highestShapeY = 0
        var shape = shapes.first()
        var shapePos = Co(3, 4 + shape.low)

        while (true) {
            // Wind push
            val pushedPos = shapePos + if (wind[turn % wind.length] == '>') Co(1, 0) else Co(-1, 0)
            if (!shape.collides(pushedPos, filled)) shapePos = pushedPos
            // Fall down
            val fellPos = shapePos + Co(0, -1)
            if (!shape.collides(fellPos, filled)) {
                shapePos = fellPos
            } else { // If it collides it rests
                filled.addAll(shape.coords.map { it + shapePos })
                highestShapeY = max(shapePos.y, highestShapeY)
                if (shapeCount == 2021) break // Termination
                shape = shapes[++shapeCount % shapes.size]
                shapePos = Co(3, highestShapeY + 4 + shape.low)
            }
            turn++
        }

        return highestShapeY
    }

    private data class State(val windIndex: Int, val shapeIndex: Int, val filledLast24: List<Co>)

    override fun part2(): Any {
        val wind = input.first()
        var turn = 0
        val filled = mutableSetOf<Co>()
        var shapeCount = 0
        var highestShapeY = 0
        var shape = shapes.first()
        var shapePos = Co(3, 4 + shape.low)
        val stateToShapeCountAndHeight = mutableMapOf<State, Pair<Int, Int>>()
        var lastHit: Pair<Int, Int>? = null

        while (true) {
            // Wind push
            val pushedPos = shapePos + if (wind[turn % wind.length] == '>') Co(1, 0) else Co(-1, 0)
            if (!shape.collides(pushedPos, filled)) shapePos = pushedPos
            // Fall down
            val fellPos = shapePos + Co(0, -1)
            if (!shape.collides(fellPos, filled)) {
                shapePos = fellPos
            } else { // If it collides it rests
                filled.addAll(shape.coords.map { it + shapePos })
                highestShapeY = max(shapePos.y, highestShapeY)
                /**/
                val state = calculateState(
                    highestShapeY = highestShapeY,
                    filled = filled,
                    windIndex = turn % wind.length,
                    shapeIndex = shapeCount % shapes.size
                )
                if (state in stateToShapeCountAndHeight) {
                    val hit = stateToShapeCountAndHeight[state]!!
                    if (lastHit != null && hit.first < lastHit.first) {
                        // We have completed the first full loop
                        val length = lastHit.first - hit.first
                        val heightDiff = lastHit.second - hit.second
                        val initialCount = hit.first
                        val initialHeight = hit.second
                        val toTheEnd = 1000000000000L - hit.first
                        val fullCycles = toTheEnd / length
                        val partialLastCycle = toTheEnd % length
                        val heightFromPartialLastCycle = stateToShapeCountAndHeight.values.first {
                            it.first.toLong() == partialLastCycle + initialCount
                        }.second - initialHeight
                        return fullCycles * heightDiff + heightFromPartialLastCycle + initialHeight
                    }
                    lastHit = hit
                    println("[shape count, height] $shapeCount, $highestShapeY == ${hit.first}, ${hit.second}")
                } else {
                    stateToShapeCountAndHeight[state] = shapeCount to highestShapeY
                }
                /**/
                shape = shapes[++shapeCount % shapes.size]
                shapePos = Co(3, highestShapeY + 4 + shape.low)
            }
            turn++
        }

        return -1
    }

    private fun Shape.collides(pos: Co, filled: Set<Co>) = coords.any { p ->
        val posP = pos + p
        posP.x < 1 || posP.x > 7 || posP.y < 1 || posP in filled
    }

    private fun calculateState(
        highestShapeY: Int,
        filled: Set<Co>,
        windIndex: Int,
        shapeIndex: Int
    ): State {
        val filledLast24 = mutableListOf<Co>() // Relative
        for (y in 0 downTo -24) {
            for (x in 1..7) {
                val co = Co(x, y)
                if (Co(0, highestShapeY) + co in filled) filledLast24.add(co)
            }
        }

        return State(windIndex, shapeIndex, filledLast24)
    }

    private fun Set<Co>.print(shape: Shape, shapePos: Co) {
        println()
        for (y in 15 downTo 0) {
            for (x in 0..8) {
                print(
                    when {
                        y == 0 -> '-'
                        x == 0 || x == 8 -> '|'
                        Co(x, y) in this -> '█'
                        Co(x, y) in shape.coords.map { it + shapePos } -> '@'
                        else -> '.'
                    }
                )
            }
            println()
        }
    }
}