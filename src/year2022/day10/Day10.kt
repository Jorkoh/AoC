package year2022.day10

import utils.Solution
import year2022.day10.Day10.Instruction.ADDX
import year2022.day10.Day10.Instruction.NOOP
import kotlin.math.abs

fun main() {
    with(Day10()) {
        testFile(::part1, 13140)
        testFile(::part2, Unit)
        solve()
    }
}

private class Day10 : Solution {
    override val day = 10
    override val year = 2022

    private enum class Instruction(val cost: Int) {
        NOOP(1), ADDX(2)
    }

    override fun part1(input: List<String>): Any {
        var register = 1
        var cycle = 0
        var result = 0

        input.forEach { l ->
            val ins = if (l[0] == 'n') NOOP else ADDX
            // Pass cycles
            repeat(ins.cost) {
                cycle += 1
                if (cycle % 40 == 20) result += register * cycle
            }
            // Process
            if (ins == ADDX) register += l.drop(5).toInt()
        }

        return result
    }

    override fun part2(input: List<String>): Any {
        var register = 1
        var cycle = 0
        val result = MutableList(6) { MutableList(40) { ' ' } }

        input.forEach { l ->
            val ins = if (l[0] == 'n') NOOP else ADDX
            // Pass cycles
            repeat(ins.cost) {
                val c = cycle % 40
                val r = cycle / 40
                if (abs(register - c) < 2) result[r][c] = '█'
                cycle += 1
            }
            // Process
            if (ins == ADDX) register += l.drop(5).toInt()
        }

        result.forEach { l -> println(l.joinToString(separator = "") { it.toString() }) }
        return Unit
    }
}
