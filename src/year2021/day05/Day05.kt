package year2021.day05

import utils.Solution

fun main() {
    with(Day05()) {
        test(::part1, 5)
        test(::part2, 12)
        solve()
    }
}

private class Day05 : Solution {
    override val day = 5
    override val year = 2021

    override fun part1(input: List<String>): Int {
        val lines = input.map { line ->
            val (start, end) = line.split(" -> ")
            val (startX, startY) = start.split(',').map { it.toInt() }
            val (endX, endY) = end.split(',').map { it.toInt() }
            Line(startX, startY, endX, endY)
        }.filter { it.orthogonal }

        val mapW = lines.maxOf { it.rangeX.last } + 1
        val mapH = lines.maxOf { it.rangeY.last } + 1

        return (0 until mapW).sumOf { x ->
            (0 until mapH).count { y ->
                lines.fold(false) { previousMatch, line ->
                    val matches = line.matches(x, y)
                    if (previousMatch && matches) return@count true
                    previousMatch || matches
                }
                false
            }
        }
    }

    override fun part2(input: List<String>): Int {
        val lines = input.map { line ->
            val (start, end) = line.split(" -> ")
            val (startX, startY) = start.split(',').map { it.toInt() }
            val (endX, endY) = end.split(',').map { it.toInt() }
            Line(startX, startY, endX, endY)
        }

        val mapW = lines.maxOf { it.rangeX.last } + 1
        val mapH = lines.maxOf { it.rangeY.last } + 1

        return (0 until mapW).sumOf { x ->
            (0 until mapH).count { y ->
                lines.fold(false) { previousMatch, line ->
                    val matches = line.matches(x, y)
                    if (previousMatch && matches) return@count true
                    previousMatch || matches
                }
                false
            }
        }
    }
}

private data class Line(val startX: Int, val startY: Int, val endX: Int, val endY: Int) {
    val orthogonal = startX == endX || startY == endY
    val rangeX = if (startX <= endX) startX..endX else endX..startX
    val rangeY = if (startY <= endY) startY..endY else endY..startY
    private val dX = (endX - startX).toFloat()
    private val dY = (endY - startY).toFloat()

    fun matches(x: Int, y: Int) = x in rangeX && y in rangeY && (orthogonal || (x - startX) / dX == (y - startY) / dY)
}
