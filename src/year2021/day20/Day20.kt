package year2021.day20

import utils.Solution

fun main() {
    with(Day20()) {
        testFile(::part1, 35)
        testFile(::part2, 3351)
        solve()
    }
}

private class Day20 : Solution {
    override val day = 20
    override val year = 2021

    override fun part1(input: List<String>): Any {
        val algorithm = input.first()
        val initialImage = input.drop(2)

        val enhanced = commonProcessing(initialImage, algorithm, 2)

        return enhanced.sumOf { row -> row.count { it == '#' } }
    }

    override fun part2(input: List<String>): Any {
        val algorithm = input.first()
        val initialImage = input.drop(2)

        val enhanced = commonProcessing(initialImage, algorithm, 50)

        return enhanced.sumOf { row -> row.count { it == '#' } }
    }

    private fun commonProcessing(initialImage: List<String>, algorithm: String, times: Int): List<String> {
        var image = initialImage.addBorders(times * 2)
        repeat(times) {
            image = (image.indices.drop(1).dropLast(1)).map { y ->
                (1 until image.first().length - 1).map { x ->
                    val range = x - 1..x + 1
                    val index = (image[y - 1].slice(range) + image[y].slice(range) + image[y + 1].slice(range))
                        .map { if (it == '#') 1 else 0 }
                        .joinToString("")
                        .toInt(2)
                    algorithm[index]
                }.joinToString("")
            }
        }
        return image
    }

    private fun List<String>.addBorders(borderSize: Int): List<String> {
        val border = ".".repeat(borderSize)
        val withVerticalBorders = this.map { "$border$it$border" }
        val horizontalBorder = List(borderSize) { ".".repeat(borderSize * 2 + this.first().length) }
        return horizontalBorder + withVerticalBorders + horizontalBorder
    }
}