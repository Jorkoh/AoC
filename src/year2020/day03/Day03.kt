package year2020.day03

import utils.Solution

fun main() {
    with(Day03()) {
        test(::part1, 7)
        test(::part2, 336)
        solve()
    }
}

private class Day03 : Solution {
    override val day = 3
    override val year = 2020

    override fun part1(input: List<String>): Any {
        val slopeX = 3
        val slopeY = 1

        return (input.indices step slopeY).withIndex().count { (step, y) ->
            input[y][(slopeX * step) % input.first().length] == '#'
        }
    }

    override fun part2(input: List<String>): Any {
        val slopesXY = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)

        return slopesXY.fold(1L) { acc, (slopeX, slopeY) ->
            (input.indices step slopeY).withIndex().count { (step, y) ->
                input[y][(slopeX * step) % input.first().length] == '#'
            } * acc
        }
    }
}