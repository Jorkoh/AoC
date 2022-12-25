package year2022.day20

import utils.Solution
import kotlin.math.abs

fun main() {
    with(Day20()) {
        testFile(::part1, 3L)
        testFile(::part2, 1623178306L)
        solve()
    }
}

class Day20 : Solution() {
    override val day = 20
    override val year = 2022

    private data class Instruction(val value: Long, var previous: Instruction?, var next: Instruction?)

    override fun part1(): Any {
        val instructions = parseInstructions()
        instructions.applyInstructions()
        return instructions.sumCoordinates()
    }

    override fun part2(): Any {
        val instructions = parseInstructions(811589153L)
        repeat(10) { instructions.applyInstructions() }
        return instructions.sumCoordinates()
    }

    private fun parseInstructions(multiplier: Long = 1L) = buildList {
        val first = Instruction(input.first().toLong() * multiplier, null, null)
        add(first)
        var previous = first
        for (s in input.drop(1)) {
            val current = Instruction(s.toLong() * multiplier, previous, null)
            add(current)
            previous.next = current
            previous = current
        }
        previous.next = first
        first.previous = previous
    }

    private fun List<Instruction>.applyInstructions() {
        for (ins in this) {
            var before = ins.previous!!
            // Remove it from its place
            before.next = ins.next
            ins.next!!.previous = before
            // Advance forward or backward as needed
            repeat(abs(ins.value % (size - 1)).toInt()) {
                before = if (ins.value >= 0L) before.next!! else before.previous!!
            }
            // Place it on its new place
            ins.previous = before
            ins.next = before.next
            ins.next!!.previous = ins
            before.next = ins
        }
    }

    /**
     * Starting from 0 sum the 1000th, 2000th and 3000th
     */
    private fun List<Instruction>.sumCoordinates(): Long {
        var sum = 0L
        var current = first { it.value == 0L }
        repeat(3) {
            repeat(1000 % input.size) { current = current.next!! }
            sum += current.value
        }
        return sum
    }

    private fun Instruction.printList(size: Int) {
        var current = this
        repeat(size - 1) {
            print("${current.value}, ")
            current = current.next!!
        }
        println(current.value)
    }
}