package year2022.day13

import utils.Solution
import year2022.day13.Day13.PacketStuff.Packet
import year2022.day13.Day13.PacketStuff.PacketNumber

fun main() {
    with(Day13()) {
        testFile(::part1, 13)
        testFile(::part2, 140)
        solve()
        benchmark()
    }
}

class Day13 : Solution() {
    override val day = 13
    override val year = 2022

    override fun part1(): Any {
        val pairs = input.chunked(3) { (a, b, _) -> a.asPacket() to b.asPacket() }
        return pairs.withIndex().sumOf { (i, pair) ->
            when (pair.first.compareTo(pair.second)) {
                -1 -> i + 1
                1 -> 0
                else -> throw IllegalStateException() // Both are the same size, can't happen
            }
        }
    }

    override fun part2(): Any {
        val dividerOne = Packet(mutableListOf(PacketNumber(2).asPacket()))
        val dividerTwo = Packet(mutableListOf(PacketNumber(6).asPacket()))
        val packets = input.filter(String::isNotBlank).map { it.asPacket() }.toMutableList().apply {
            add(dividerOne)
            add(dividerTwo)
        }.sorted()
        return (packets.indexOf(dividerOne) + 1) * (packets.indexOf(dividerTwo) + 1)
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

    private sealed interface PacketStuff {

        data class PacketNumber(val number: Int) : PacketStuff {
            fun asPacket() = Packet().also { p -> p.stuff.add(this) }
        }

        data class Packet(val stuff: MutableList<PacketStuff> = mutableListOf()) : PacketStuff, Comparable<Packet> {
            // -1 if first is smaller, 1 if first is bigger, 0 if the same
            override fun compareTo(other: Packet): Int {
                var i = 0
                while (i < this.stuff.size && i < other.stuff.size) {
                    val a = this.stuff[i]
                    val b = other.stuff[i]
                    val order = when {
                        a is PacketNumber && b is PacketNumber -> {
                            when {
                                a.number < b.number -> -1
                                a.number > b.number -> 1
                                else -> 0
                            }
                        }
                        a is Packet && b is Packet -> a.compareTo(b)
                        a is Packet && b is PacketNumber -> a.compareTo(b.asPacket())
                        a is PacketNumber && b is Packet -> a.asPacket().compareTo(b)
                        else -> throw IllegalStateException()
                    }
                    if (order != 0) return order
                    i++
                }
                // If we got here one of the packets run out
                return when {
                    this.stuff.size < other.stuff.size -> -1
                    this.stuff.size > other.stuff.size -> 1
                    else -> 0
                }
            }
        }
    }
}