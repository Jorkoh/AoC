package year2021.day25

import utils.Solution

fun main() {
    with(Day25()) {
        test(::part1, 58)
        solve()
    }
}

private class Day25 : Solution {
    override val day = 25
    override val year = 2021

    override fun part1(input: List<String>): Any {
        var map = input
        val w = map.first().length
        val h = map.size
        var moves = 0

        do {
            moves++
            var moved = false
            map = map.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    when {
                        c == '.' && map[y][(x + w - 1) % w] == '>' -> '>'
                        c == '>' && map[y][(x + w + 1) % w] == '.' -> '.'
                        else -> c
                    }.also { if (it != c) moved = true }
                }.joinToString("")
            }
            map = map.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    when {
                        c == '.' && map[(y + h - 1) % h][x] == 'v' -> 'v'
                        c == 'v' && map[(y + h + 1) % h][x] == '.' -> '.'
                        else -> c
                    }.also { if (it != c) moved = true }
                }.joinToString("")
            }
        } while (moved)

        return moves
    }

    override fun part2(input: List<String>): Any {
        return 0
    }
}