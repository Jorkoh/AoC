package year2015.day15

import utils.Solution
import kotlin.math.max

fun main() {
    with(Day15()) {
        testFile(::testPart1, 62842880L)
        testFile(::testPart2, 57600000L)
        solve()
    }
}

// This solution should be rewritten in a way that doesn't depend on a
// fixed amount of ingredients and a fixed amount of properties
class Day15 : Solution() {
    override val day = 15
    override val year = 2015

    fun testPart1(): Any {
        check(input.size == 2)
        val teaspoons = 100

        val ingredients = input.map { line ->
            val parts = line.split(" ")

            Ingredient(
                capacity = parts[2].dropLast(1).toInt(),
                durability = parts[4].dropLast(1).toInt(),
                flavor = parts[6].dropLast(1).toInt(),
                texture = parts[8].dropLast(1).toInt(),
                calories = parts[10].toInt(),
            )
        }

        var highestScore = 0L

        for (amount1 in 0..teaspoons) {
            for (amount2 in 0..teaspoons) {
                val amounts = listOf(amount1, amount2)

                // This can be done smarter
                if (amounts.sum() != teaspoons) continue

                highestScore = max(highestScore, calculateScore(amounts, ingredients))
            }
        }

        return highestScore
    }

    override fun part1(): Any {
        check(input.size == 4)
        val teaspoons = 100

        val ingredients = input.map { line ->
            val parts = line.split(" ")

            Ingredient(
                capacity = parts[2].dropLast(1).toInt(),
                durability = parts[4].dropLast(1).toInt(),
                flavor = parts[6].dropLast(1).toInt(),
                texture = parts[8].dropLast(1).toInt(),
                calories = parts[10].toInt(),
            )
        }

        var highestScore = 0L

        for (amount1 in 0..teaspoons) {
            for (amount2 in 0..teaspoons) {
                for (amount3 in 0..teaspoons) {
                    for (amount4 in 0..teaspoons) {
                        val amounts = listOf(amount1, amount2, amount3, amount4)

                        // This can be done smarter
                        if (amounts.sum() != teaspoons) continue

                        highestScore = max(highestScore, calculateScore(amounts, ingredients))
                    }
                }
            }
        }

        return highestScore
    }

    fun testPart2(): Any {
        check(input.size == 2)
        val teaspoons = 100
        val calorieTarget = 500

        val ingredients = input.map { line ->
            val parts = line.split(" ")

            Ingredient(
                capacity = parts[2].dropLast(1).toInt(),
                durability = parts[4].dropLast(1).toInt(),
                flavor = parts[6].dropLast(1).toInt(),
                texture = parts[8].dropLast(1).toInt(),
                calories = parts[10].toInt(),
            )
        }

        var highestScore = 0L

        for (amount1 in 0..teaspoons) {
            for (amount2 in 0..teaspoons) {
                val amounts = listOf(amount1, amount2)

                // This can be done smarter
                if (amounts.sum() != teaspoons) continue
                if (calculateCalories(amounts, ingredients) != calorieTarget) continue

                highestScore = max(highestScore, calculateScore(amounts, ingredients))
            }
        }

        return highestScore
    }

    override fun part2(): Any {
        check(input.size == 4)
        val teaspoons = 100
        val calorieTarget = 500

        val ingredients = input.map { line ->
            val parts = line.split(" ")

            Ingredient(
                capacity = parts[2].dropLast(1).toInt(),
                durability = parts[4].dropLast(1).toInt(),
                flavor = parts[6].dropLast(1).toInt(),
                texture = parts[8].dropLast(1).toInt(),
                calories = parts[10].toInt(),
            )
        }

        var highestScore = 0L

        for (amount1 in 0..teaspoons) {
            for (amount2 in 0..teaspoons) {
                for (amount3 in 0..teaspoons) {
                    for (amount4 in 0..teaspoons) {
                        val amounts = listOf(amount1, amount2, amount3, amount4)

                        // This can be done smarter
                        if (amounts.sum() != teaspoons) continue
                        if (calculateCalories(amounts, ingredients) != calorieTarget) continue

                        highestScore = max(highestScore, calculateScore(amounts, ingredients))
                    }
                }
            }
        }

        return highestScore
    }

    private fun calculateCalories(amounts: List<Int>, ingredients: List<Ingredient>) = amounts.withIndex()
        .sumOf { (i, amount) -> amount * ingredients[i].calories }

    private fun calculateScore(amounts: List<Int>, ingredients: List<Ingredient>): Long {
        val capacityTotal = amounts.withIndex().sumOf { (i, amount) ->
            amount * ingredients[i].capacity
        }.coerceAtLeast(0)
        val durabilityTotal = amounts.withIndex().sumOf { (i, amount) ->
            amount * ingredients[i].durability
        }.coerceAtLeast(0)
        val flavorTotal = amounts.withIndex().sumOf { (i, amount) ->
            amount * ingredients[i].flavor
        }.coerceAtLeast(0)
        val textureTotal = amounts.withIndex().sumOf { (i, amount) ->
            amount * ingredients[i].texture
        }.coerceAtLeast(0)

        return 1L * capacityTotal * durabilityTotal * flavorTotal * textureTotal
    }

    data class Ingredient(val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)
}