package year2015.day16

import utils.Solution

fun main() {
    with(Day16()) {
        solve()
        benchmark()
    }
}

class Day16 : Solution() {
    override val day = 16
    override val year = 2015

    private val part1Checks = listOf<Pair<String, (Int) -> Boolean>>(
        "children" to { it == 3 }, "cats" to { it == 7 }, "samoyeds" to { it == 2 },
        "pomeranians" to { it == 3 }, "akitas" to { it == 0 }, "vizslas" to { it == 0 },
        "goldfish" to { it == 5 }, "trees" to { it == 3 }, "cars" to { it == 2 },
        "perfumes" to { it == 1 },
    )

    private val part2Checks = listOf<Pair<String, (Int) -> Boolean>>(
        "children" to { it == 3 }, "cats" to { it > 7 }, "samoyeds" to { it == 2 },
        "pomeranians" to { it < 3 }, "akitas" to { it == 0 }, "vizslas" to { it == 0 },
        "goldfish" to { it < 5 }, "trees" to { it > 3 }, "cars" to { it == 2 },
        "perfumes" to { it == 1 },
    )

    override fun part1(): Any {
        return input.mapAunts().indexOfFirst { aunt ->
            part1Checks.all { (k, v) -> k !in aunt || v(aunt[k]!!) }
        } + 1
    }

    override fun part2(): Any {
        return input.mapAunts().indexOfFirst { aunt ->
            part2Checks.all { (k, v) -> k !in aunt || v(aunt[k]!!) }
        } + 1
    }

    private fun List<String>.mapAunts() = map { l ->
        l.dropWhile { c -> c != ':' }.drop(2).split(", ").associate {
            val (k, v) = it.split(": ")
            k to v.toInt()
        }
    }
}