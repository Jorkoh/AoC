package year2021.day16

import utils.Solution
import year2021.day16.Day16.LengthType.Companion.toLengthType
import year2021.day16.Day16.PacketType.Companion.toPacketType

fun main() {
    with(Day16()) {
        testFile(::part1Test, listOf(16, 12, 23, 31), "test1")
        testFile(::part2Test, listOf(3L, 54L, 7L, 9L, 1L, 0L, 0L, 1L), "test2")
        solve()
    }
}

class Day16 : Solution() {
    override val day = 16
    override val year = 2021

    private val versions = mutableListOf<Int>()

    override fun part1(): Any {
        val reader = Reader(input.first().hexToBin())
        versions.clear()
        processPacket(reader)
        return versions.sum()
    }

    fun part1Test(): Any {
        return input.map {
            val reader = Reader(it.hexToBin())
            versions.clear()
            processPacket(reader)
            versions.sum()
        }
    }

    override fun part2(): Any {
        val reader = Reader(input.first().hexToBin())
        return processPacket(reader)
    }

    fun part2Test(): Any {
        return input.map {
            val reader = Reader(it.hexToBin())
            processPacket(reader)
        }
    }

    private fun processPacket(reader: Reader): Long {
        val version = reader.readAsInt(3)
        versions.add(version)

        return when (val packetType = reader.readAsInt(3).toPacketType()) {
            PacketType.LiteralValue -> processLiteral(reader)
            else -> processOperator(reader, packetType)
        }
    }

    private fun processLiteral(reader: Reader) = buildString {
        while (reader.read(1) != "0") append(reader.read(4))
        append(reader.read(4))
    }.binToLong()

    private fun processOperator(
        reader: Reader,
        packetType: PacketType
    ): Long {
        val lengthType = reader.read(1).toLengthType()
        val length = when (lengthType) {
            LengthType.Total -> reader.readAsLong(15)
            LengthType.SubPacketCount -> reader.readAsLong(11)
        }

        val subPackets = mutableListOf<Long>()
        val start = reader.pointer
        var done = false
        var subPacketsProcessed = 0

        while (!done) {
            subPackets.add(processPacket(reader))
            subPacketsProcessed += 1
            done = when (lengthType) {
                LengthType.Total -> reader.pointer >= start + length
                LengthType.SubPacketCount -> subPacketsProcessed >= length
            }
        }

        return when (packetType) {
            PacketType.Sum -> subPackets.sum()
            PacketType.Product -> subPackets.fold(1) { acc, value -> acc * value }
            PacketType.Minimum -> subPackets.minOf { it }
            PacketType.Maximum -> subPackets.maxOf { it }
            PacketType.GreaterThan -> if (subPackets[0] > subPackets[1]) 1 else 0
            PacketType.LessThan -> if (subPackets[0] < subPackets[1]) 1 else 0
            PacketType.EqualTo -> if (subPackets[0] == subPackets[1]) 1 else 0
            else -> throw IllegalStateException("Expected operator packet type and got ${packetType.name}")
        }
    }

    private inner class Reader(val raw: String, var pointer: Int = 0) {
        fun read(count: Int): String {
            return raw.slice(pointer until pointer + count).also { pointer += count }
        }

        fun readAsInt(count: Int) = read(count).binToInt()
        fun readAsLong(count: Int) = read(count).binToLong()
    }

    private enum class PacketType {
        Sum, Product,
        Minimum, Maximum,
        LiteralValue,
        GreaterThan, LessThan, EqualTo;

        companion object {
            fun Int.toPacketType() = values()[this.toInt()]
        }
    }

    private enum class LengthType {
        Total, SubPacketCount;

        companion object {
            fun String.toLengthType() = when (this) {
                "0" -> Total
                "1" -> SubPacketCount
                else -> throw IllegalStateException("Length type needs to be 0 or 1 and was $this")
            }
        }
    }

    private fun String.hexToBin(): String = buildString {
        this@hexToBin.forEach {
            val binaryString = when (it) {
                '0' -> "0000"
                '1' -> "0001"
                '2' -> "0010"
                '3' -> "0011"
                '4' -> "0100"
                '5' -> "0101"
                '6' -> "0110"
                '7' -> "0111"
                '8' -> "1000"
                '9' -> "1001"
                'A' -> "1010"
                'B' -> "1011"
                'C' -> "1100"
                'D' -> "1101"
                'E' -> "1110"
                'F' -> "1111"
                else -> throw IllegalStateException("Character was not hex, it was $it")
            }
            append(binaryString)
        }
    }

    private fun String.binToInt(): Int = toInt(2)
    private fun String.binToLong(): Long = toLong(2)
}