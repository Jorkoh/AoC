package year2015.day20

import utils.Solution
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    with(Day20()) {
        test(::part1, 8, "150")
        solve()
    }
}

class Day20 : Solution() {
    override val day = 20
    override val year = 2015

    override fun part1(): Any {
        val target = input.first().toInt()

        return (1..Int.MAX_VALUE).first { n -> n.getDivisors().sumOf { it * 10 } >= target }
    }

    override fun part2(): Any {
        val target = input.first().toInt()
        val divisorUseCount = mutableMapOf<Int, Int>()

        return (1..Int.MAX_VALUE).first { n ->
            n.getDivisors().filterNot { d ->
                // Only use those with less than 50 uses
                (divisorUseCount[d] ?: 0) >= 50
            }.sumOf { d ->
                // Since it's used increase the count
                divisorUseCount[d] = (divisorUseCount[d] ?: 0) + 1
                d * 11
            } >= target
        }
    }

    private fun Int.getDivisors() = buildList {
        val upperBound = ceil(sqrt(this@getDivisors.toDouble())).roundToInt()
        for (n in 1..upperBound) {
            if (this@getDivisors % n == 0) {
                add(n)
                val quotient = this@getDivisors / n
                if (quotient > upperBound) add(quotient)
            }
        }
    }
}