package year2015.day21

import utils.Solution
import kotlin.math.max
import kotlin.math.min

fun main() {
    with(Day21()) {
        solve()
    }
}

// Nothing fancy, some combinations are redundant
class Day21 : Solution() {
    override val day = 21
    override val year = 2015

    private data class Item(val cost: Int, val damage: Int = 0, val armor: Int = 0)

    private val weapons = listOf(
        Item(8, damage = 4),
        Item(10, damage = 5),
        Item(25, damage = 6),
        Item(40, damage = 7),
        Item(74, damage = 8),
    )
    private val armors = listOf(
        Item(0), // Representing the empty option like this
        Item(13, armor = 1),
        Item(31, armor = 2),
        Item(53, armor = 3),
        Item(75, armor = 4),
        Item(102, armor = 5),
    )
    private val rings = listOf(
        Item(0), // Representing an empty option for each hand like this
        Item(0), // Representing an empty option for each hand like this
        Item(25, damage = 1),
        Item(50, damage = 2),
        Item(100, damage = 3),
        Item(20, armor = 1),
        Item(40, armor = 2),
        Item(80, armor = 3),
    )

    private data class Character(var hp: Int, val damage: Int, val armor: Int)

    override fun part1(): Any {
        val fullHpBoss = getBoss()

        var cheapestWin = Int.MAX_VALUE
        for (w in weapons) {
            for (a in armors) {
                for (r1 in rings) {
                    for (r2 in rings) {
                        val cost = w.cost + a.cost + r1.cost + r2.cost
                        if (cost >= cheapestWin) continue // no chance to improve the score

                        val player = Character(
                            hp = 100,
                            damage = w.damage + r1.damage + r2.damage,
                            armor = a.armor + r1.armor + r2.armor
                        )
                        val boss = fullHpBoss.copy()
                        if (playerWins(player, boss)) cheapestWin = min(cheapestWin, cost)
                    }
                }
            }
        }
        return cheapestWin
    }

    override fun part2(): Any {
        val fullHpBoss = getBoss()

        var mostExpensiveLose = Int.MIN_VALUE
        for (w in weapons) {
            for (a in armors) {
                for (r1 in rings) {
                    for (r2 in rings) {
                        val cost = w.cost + a.cost + r1.cost + r2.cost
                        if (cost <= mostExpensiveLose) continue // no chance to improve the score

                        val player = Character(
                            hp = 100,
                            damage = w.damage + r1.damage + r2.damage,
                            armor = a.armor + r1.armor + r2.armor
                        )
                        val boss = fullHpBoss.copy()
                        if (!playerWins(player, boss)) mostExpensiveLose = max(mostExpensiveLose, cost)
                    }
                }
            }
        }
        return mostExpensiveLose
    }

    private fun getBoss() = Character(
        hp = input[0].split(": ").last().toInt(),
        damage = input[1].split(": ").last().toInt(),
        armor = input[2].split(": ").last().toInt()
    )

    private fun playerWins(player: Character, boss: Character): Boolean {
        while (player.hp > 0) {
            // Player attacks first
            boss.hp -= (player.damage - boss.armor).coerceAtLeast(1)
            if (boss.hp <= 0) return true
            player.hp -= (boss.damage - player.armor).coerceAtLeast(1)
        }
        return false
    }
}