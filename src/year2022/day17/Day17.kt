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
        var count = 0
        var height = 0
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
                height = max(shapePos.y, height)
                if (count == 2021) break // Termination
                shape = shapes[++count % shapes.size]
                shapePos = Co(3, height + 4 + shape.low)
            }
            turn++
        }

        return height
    }

    private data class Stats(val count: Int, val height: Int)
    private data class State(val windIndex: Int, val shapeIndex: Int, val lastFilled: List<Co>)

    override fun part2(): Any {
        val wind = input.first()
        var turn = 0
        val filled = mutableSetOf<Co>()
        var count = 1
        var height = 0
        var shape = shapes.first()
        var shapePos = Co(3, 4 + shape.low)
        val statesToStats = mutableMapOf<State, Stats>()

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
                height = max(shapePos.y, height)
                // Check if we loop. If state looped we can immediately calculate the remaining
                val state = State(
                    windIndex = turn % wind.length,
                    shapeIndex = (count - 1) % shapes.size,
                    lastFilled = filled.getLastFilled(height)
                )
                if (state in statesToStats) return calculateAtEnd(statesToStats, state, count, height)
                else statesToStats[state] = Stats(count, height)
                // Spawn next
                count++
                shape = shapes[(count - 1) % shapes.size]
                shapePos = Co(3, height + 4 + shape.low)
            }
            turn++
        }
    }

    private fun calculateAtEnd(
        statesToStats: MutableMap<State, Stats>,
        state: State,
        count: Int,
        height: Int
    ): Long {
        val start = statesToStats[state]!!
        val loopLength = count - start.count
        val loopHeightGain = height - start.height
        val remaining = 1000000000000L - start.count
        val remainingCycles = remaining / loopLength
        val partialLastCycle = (remaining % loopLength).toInt()
        val heightFromPartialLastCycle = statesToStats.values.first { (vCount, _) ->
            vCount == partialLastCycle + start.count
        }.height - start.height
        return remainingCycles * loopHeightGain + heightFromPartialLastCycle + start.height
    }

    private fun Shape.collides(pos: Co, filled: Set<Co>) = coords.any { p ->
        val posP = pos + p
        posP.x < 1 || posP.x > 7 || posP.y < 1 || posP in filled
    }

    private fun Set<Co>.getLastFilled(height: Int, range: Int = 24) = buildList {
        for (y in 0 downTo -range) {
            for (x in 1..7) {
                val co = Co(x, y)
                if (Co(0, height) + co in this@getLastFilled) add(co)
            }
        }
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