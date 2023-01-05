package year2022.day11

import utils.Solution

fun main() {
    with(Day11()) {
        lcm = 23L * 19L * 13L * 17L
        testFile(::part1, 10605L)
        testFile(::part2, 2713310158L)
        lcm = 11L * 7L * 3L * 5L * 17L * 13L * 19L * 2L
        solve()
    }
}

class Day11 : Solution() {
    override val day = 11
    override val year = 2022

    override fun part1(): Any {
        val monkeys = parseMonkeys()
        val inspectionCounts = MutableList(monkeys.size) { 0L }
        repeat(20) {
            monkeys.forEachIndexed { i, monkey ->
                inspectionCounts[i] += monkey.items.size.toLong()
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst().apply {
                        value = monkey.onInspection(value)
                        value /= 3
                    }
                    val destination = monkey.throwTowards(item.value)
                    monkeys[destination].items.addLast(item.also { it.history.add(destination) })
                }
            }
        }
        return inspectionCounts.sortedDescending().let { it[0] * it[1] }
    }

    var lcm = 0L

    override fun part2(): Any {
        val monkeys = parseMonkeys()
        val inspectionCounts = MutableList(monkeys.size) { 0L }
        repeat(10000) {
            monkeys.forEachIndexed { i, monkey ->
                inspectionCounts[i] += monkey.items.size.toLong()
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst().apply {
                        value = monkey.onInspection(value)
                        value %= lcm
                    }
                    val destination = monkey.throwTowards(item.value)
                    monkeys[destination].items.addLast(item.also { it.history.add(destination) })
                }
            }
        }
        return inspectionCounts.sortedDescending().let { it[0] * it[1] }
    }

    private data class Monkey(
        val items: ArrayDeque<Item>,
        val onInspection: (Long) -> Long,
        val throwTowards: (Long) -> Int
    )

    private data class Item(
        var value: Long,
        val history: MutableList<Int>
    )

    private fun parseMonkeys(): List<Monkey> = List((input.size + 1) / 7) { i ->
        val offset = i * 7
        val items = input[offset + 1].drop(18).split(", ").map { Item(it.toLong(), mutableListOf(i)) }
        val onInspection: (Long) -> Long = { old ->
            val (a, opRaw, b) = input[offset + 2].drop(19).split(" ")
            val op: (Long, Long) -> Long = when (opRaw) {
                "+" -> { a, b -> a + b }
                "*" -> { a, b -> a * b }
                else -> throw IllegalStateException()
            }
            op(if (a == "old") old else a.toLong(), if (b == "old") old else b.toLong())
        }
        val throwTowards: (Long) -> Int = {
            if (it % input[offset + 3].drop(21).toLong() == 0L) {
                input[offset + 4].drop(29).toInt()
            } else {
                input[offset + 5].drop(30).toInt()
            }
        }
        Monkey(
            items = ArrayDeque(items),
            onInspection = onInspection,
            throwTowards = throwTowards
        )
    }
}
