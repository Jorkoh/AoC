package year2022.day19

import utils.Solution
import kotlin.math.max

fun main() {
    with(Day19()) {
        testFile(::part1, 33)
//        testFile(::part2, Unit)
        solve()
    }
}

class Day19 : Solution() {
    override val day = 19
    override val year = 2022

    private data class Blueprint(
        val oreBotOreCost: Int,
        val clayBotOreCost: Int,
        val obsBotOreCost: Int,
        val obsBotClayCost: Int,
        val geoBotOreCost: Int,
        val geoBotObsCost: Int
    )

    private data class State(
        val ore: Int = 0,
        val clay: Int = 0,
        val obs: Int = 0,
        val geo: Int = 0,
        val oreBots: Int = 1,
        val clayBots: Int = 0,
        val obsBots: Int = 0,
        val geoBots: Int = 0,
        val minute: Int = 1
    )

    override fun part1(): Any {
        val blueprints = input.map { l ->
            val parts = l.split(" ")
            Blueprint(
                oreBotOreCost = parts[6].toInt(),
                clayBotOreCost = parts[12].toInt(),
                obsBotOreCost = parts[18].toInt(),
                obsBotClayCost = parts[21].toInt(),
                geoBotOreCost = parts[27].toInt(),
                geoBotObsCost = parts[30].toInt()
            )
        }
        return blueprints.withIndex().sumOf { (i, bp) -> (i + 1) * bp.maxGeodes(24) }
    }

    private fun Blueprint.maxGeodes(lastMinute: Int): Int {
        val seen = mutableSetOf<State>()
        var maxGeode = -1
        var maxTime = 0

        val frontier = ArrayDeque(listOf(State()))
        while (frontier.isNotEmpty()) {
            val c = frontier.removeFirst()
            if (c.minute > maxTime) {
                maxTime = c.minute
                seen.clear()
                print("$maxTime, ")
            }

            if (c.minute == lastMinute) {
                val atEnd = c.collect() // On the last turn only collect
                maxGeode = max(maxGeode, atEnd.geo)
            } else {
                if (!c.passesMinimums(
                        geoBotObsCost,
                        obsBotClayCost,
                        clayBotOreCost
                    )
                ) continue // Check for minimum requisites to cull under-performers
                for (o in c.options(this)) {
                    if (o !in seen) {
                        frontier.addLast(o)
                        seen.add(o)
                    }
                }
            }
        }

        println("\nMax geode: $maxGeode")
        return maxGeode
    }

    private fun State.collect() = copy(
        ore = ore + oreBots,
        clay = clay + clayBots,
        obs = obs + obsBots,
        geo = geo + geoBots,
        minute = minute + 1
    )

    private fun State.options(bp: Blueprint) = buildList {
        val c = collect()
        add(c) // Don't build anything
        if (ore >= bp.oreBotOreCost) { // Build ore robot
            add(c.copy(ore = c.ore - bp.oreBotOreCost, oreBots = c.oreBots + 1))
        }
        if (ore >= bp.clayBotOreCost) { // Build clay robot
            add(c.copy(ore = c.ore - bp.clayBotOreCost, clayBots = c.clayBots + 1))
        }
        if (ore >= bp.obsBotOreCost && clay >= bp.obsBotClayCost) { // Build obsidian robot
            add(c.copy(ore = c.ore - bp.obsBotOreCost, clay = c.clay - bp.obsBotClayCost, obsBots = c.obsBots + 1))
        }
        if (ore >= bp.geoBotOreCost && obs >= bp.geoBotObsCost) { // Build geode robot
            add(collect().copy(ore = c.ore - bp.geoBotOreCost, obs = c.obs - bp.geoBotObsCost, geoBots = c.geoBots + 1))
        }
    }

    private fun State.passesMinimums(geoBotObsCost: Int, obsBotClayCost: Int, clayBotOreCost: Int) =
        when { // All of this are strict minimums. If it doesn't pass it won't generate any geode at all
            // Need to have or create a geo bot during 23, so it produces at least the first geo during 24.
            minute == 23 && !(geoBots > 0 || obs < geoBotObsCost) -> false
            // TODO this can be tightened
            // Need to have or create an obs bot during 22, so it produces at least the first obs during 23.
            minute == 22 && !(obsBots > 0 || clay < obsBotClayCost) -> false
            // TODO this can be tightened
            // Need to have or create a clay bot during 21, so it produces at least the first clay during 22.
            minute == 21 && !(clayBots > 0 || ore < clayBotOreCost) -> false
            else -> true
        }

    override fun part2(): Any {
        return Unit
    }
}