package year2022.day13

import utils.Solution
import year2022.day13.Day13.PacketStuff.Packet
import year2022.day13.Day13.PacketStuff.PacketNumber

fun main() {
    with(Day13()) {
        testFile(::part1, 13)
//        testFile(::part2, Unit)
//        solve()
    }
}

class Day13 : Solution() {
    override val day = 13
    override val year = 2022

    private sealed interface PacketStuff {
        data class Packet(val stuff: MutableList<PacketStuff> = mutableListOf()) : PacketStuff
        data class PacketNumber(val number: Int) : PacketStuff
    }

    override fun part1(): Any {
        val pairs = input.chunked(3) { (a, b, _) -> a.asPacket() to b.asPacket() }
        return pairs.withIndex().sumOf { (i, pair) ->
            if (pair.isInRightOrder()) {
                println("right order: ${pair.first} - ${pair.second}")
                i
            } else {
                println("wrong order: ${pair.first} - ${pair.second}")
                0
            }
        }
    }

    override fun part2(): Any {
        return Unit
    }

    private fun String.asPacket(): Packet = Packet().apply {
        var parsed = 1
        while (parsed < length) {
            var opened = 0
            val chunk = drop(parsed).takeWhile { c ->
                if (c == '[') opened++
                val take = c.isDigit() || opened > 0
                if (c == ']') opened--
                take
            }
            parsed += chunk.length + 1 // skip separator
            if (chunk.isNotEmpty()) { // empty packets are a thing too
                val thing = if (chunk[0].isDigit()) PacketNumber(chunk.toInt()) else chunk.asPacket()
                stuff.add(thing)
            }
        }
    }

    private fun Pair<Packet, Packet>.isInRightOrder(): Boolean {
        return true
    }
}