package year2015.day07

import utils.Solution

fun main() {
    with(Day07()) {
        solve()
    }
}

private class Day07 : Solution {
    override val day = 7
    override val year = 2015

    override fun part1(input: List<String>): Any {
        val instructions = input.parseInstructions()
        val signals = mutableMapOf<String, UShort>()

        return instructions.first { it.out.id == "a" }.evaluate(instructions, signals)
    }


    override fun part2(input: List<String>): Any {
        val instructions = input.parseInstructions()
        val signals = mutableMapOf<String, UShort>()

        val aSignal = instructions.first { it.out.id == "a" }.evaluate(instructions, signals)
        signals.clear()
        signals["b"] = aSignal
        return instructions.first { it.out.id == "a" }.evaluate(instructions, signals)
    }

    private fun Instruction.evaluate(instructions: List<Instruction>, signals: MutableMap<String, UShort>): UShort {
        return when (this) {
            is Instruction.And -> input1.evaluate(instructions, signals) and input2.evaluate(instructions, signals)
            is Instruction.Or -> input1.evaluate(instructions, signals) or input2.evaluate(instructions, signals)
            is Instruction.LShift -> input.evaluate(instructions, signals).shl(places)
            is Instruction.RShift -> input.evaluate(instructions, signals).shr(places)
            is Instruction.Not -> input.evaluate(instructions, signals).inv()
            is Instruction.Pass -> input.evaluate(instructions, signals)
        }
    }

    private fun Connection.evaluate(instructions: List<Instruction>, signals: MutableMap<String, UShort>): UShort =
        when (this) {
            is Connection.Value -> signal
            is Connection.Wire -> {
                if (id !in signals) {
                    val signal = instructions.first { it.out.id == id }.evaluate(instructions, signals)
                    signals[id] = signal
                }
                signals[id]!!
            }
        }

    private fun List<String>.parseInstructions() = map { line ->
        val parts = line.split(' ')
        when {
            line.contains("AND") -> {
                Instruction.And(
                    input1 = parts[0].asConnection(),
                    input2 = parts[2].asConnection(),
                    out = Connection.Wire(parts[4])
                )
            }
            line.contains("OR") -> {
                Instruction.Or(
                    input1 = parts[0].asConnection(),
                    input2 = parts[2].asConnection(),
                    out = Connection.Wire(parts[4])
                )
            }
            line.contains("LSHIFT") -> {
                Instruction.LShift(
                    input = parts[0].asConnection(),
                    places = parts[2].toInt(),
                    out = Connection.Wire(parts[4])
                )
            }
            line.contains("RSHIFT") -> {
                Instruction.RShift(
                    input = parts[0].asConnection(),
                    places = parts[2].toInt(),
                    out = Connection.Wire(parts[4])
                )
            }
            line.contains("NOT") -> {
                Instruction.Not(
                    input = parts[1].asConnection(),
                    out = Connection.Wire(parts[3])
                )
            }
            else -> {
                Instruction.Pass(
                    input = parts[0].asConnection(),
                    out = Connection.Wire(parts[2])
                )
            }
        }
    }

    private fun String.asConnection(): Connection =
        this.toIntOrNull()?.let { Connection.Value(it.toUShort()) } ?: Connection.Wire(this)

    private sealed class Instruction(val out: Connection.Wire) {
        class And(val input1: Connection, val input2: Connection, out: Connection.Wire) : Instruction(out)
        class Or(val input1: Connection, val input2: Connection, out: Connection.Wire) : Instruction(out)
        class LShift(val input: Connection, val places: Int, out: Connection.Wire) : Instruction(out)
        class RShift(val input: Connection, val places: Int, out: Connection.Wire) : Instruction(out)
        class Not(val input: Connection, out: Connection.Wire) : Instruction(out)
        class Pass(val input: Connection, out: Connection.Wire) : Instruction(out)
    }

    private sealed class Connection {
        data class Value(val signal: UShort) : Connection()
        data class Wire(val id: String) : Connection()
    }

    private infix fun UShort.shl(bitCount: Int) = (this.toUInt() shl bitCount).toUShort()

    private infix fun UShort.shr(bitCount: Int) = (this.toUInt() shr bitCount).toUShort()
}