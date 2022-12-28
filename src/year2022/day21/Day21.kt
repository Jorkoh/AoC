package year2022.day21

import utils.Solution

fun main() {
    with(Day21()) {
        testFile(::part1, 152L)
        testFile(::part2, 301L)
        solve()
    }
}

class Day21 : Solution() {
    override val day = 21
    override val year = 2022

    override fun part1(): Any {
        val monkeys = input.associate { it.take(4) to it.drop(6) }
        return monkeys.eval("root")
    }

    override fun part2(): Any {
        val monkeys = input.associate { it.take(4) to it.drop(6) }.toMutableMap()
        monkeys["humn"] = "x"
        
        val (idA, _, idB) = monkeys["root"]!!.split(' ')
        val leftResult = monkeys.evalProper(idA)
        val rightResult = monkeys.evalProper(idB)
        
        val raw =  if (leftResult.xs != 0.0) (rightResult.cs - leftResult.cs) / leftResult.xs
        else (leftResult.cs - rightResult.cs) / rightResult.xs
        
        return raw.toLong()
    }

    private fun Map<String, String>.eval(id: String): Long {
        val ins = get(id)!!
        return if (ins.first().isDigit()) {
            ins.toLong()
        } else {
            val (idA, op, idB) = ins.split(' ')
            when (op) {
                "+" -> eval(idA) + eval(idB)
                "-" -> eval(idA) - eval(idB)
                "*" -> eval(idA) * eval(idB)
                "/" -> eval(idA) / eval(idB)
                else -> throw IllegalStateException()
            }
        }
    }

    data class Term(val xs: Double, val cs: Double)

    operator fun Term.plus(b: Term) = Term(xs + b.xs, cs + b.cs)
    operator fun Term.minus(b: Term) = Term(xs - b.xs, cs - b.cs)
    operator fun Term.times(b: Term): Term {
        if (xs != 0.0 && b.xs != 0.0) throw IllegalStateException()
        return Term(xs * b.cs + cs * b.xs, cs * b.cs)
    }

    operator fun Term.div(b: Term): Term {
        if (b.xs != 0.0) throw IllegalStateException()
        return Term(xs / b.cs, cs / b.cs)
    }

    private fun Map<String, String>.evalProper(id: String): Term {
        val ins = get(id)!!
        return when {
            ins.first().isDigit() -> Term(0.0, ins.toDouble())
            ins == "x" -> Term(1.0, 0.0)
            else -> {
                val (idA, op, idB) = ins.split(' ')
                when (op) {
                    "+" -> evalProper(idA) + evalProper(idB)
                    "-" -> evalProper(idA) - evalProper(idB)
                    "*" -> evalProper(idA) * evalProper(idB)
                    "/" -> evalProper(idA) / evalProper(idB)
                    else -> throw IllegalStateException()
                }
            }
        }
    }
}
