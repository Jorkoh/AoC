package year2020.day04

import utils.Solution


fun main() {
    with(Day04()) {
        testFile(::part1, 2, "test1")
        testFile(::part2, 0, "test2")
        testFile(::part2, 4, "test3")
        solve()
    }
}

class Day04 : Solution() {
    override val day = 4
    override val year = 2020

    override fun part1(): Any {
        val passports = buildList<List<Pair<String, String>>> {
            var fields = mutableListOf<Pair<String, String>>()
            for (line in input) {
                if (line.isBlank()) {
                    add(fields)
                    fields = mutableListOf()
                } else {
                    fields += line.split(' ').map {
                        val (key, value) = it.split(':')
                        key to value
                    }
                }
            }
            add(fields)
        }

        val required = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        return passports.count { passport -> passport.map { it.first }.containsAll(required) }
    }

    override fun part2(): Any {
        val passports = buildList<List<Pair<String, String>>> {
            var fields = mutableListOf<Pair<String, String>>()
            for (line in input) {
                if (line.isBlank()) {
                    add(fields)
                    fields = mutableListOf()
                } else {
                    fields += line.split(' ').map {
                        val (key, value) = it.split(':')
                        key to value
                    }
                }
            }
            add(fields)
        }

        val required = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        return passports.count { passport ->
            passport.map { it.first }.containsAll(required) && passport.all { (key, value) ->
                when (key) {
                    "byr" -> value.toIntOrNull() in 1920..2020
                    "iyr" -> value.toIntOrNull() in 2010..2020
                    "eyr" -> value.toIntOrNull() in 2020..2030
                    "hgt" -> when {
                        value.endsWith("cm") -> value.dropLast(2).toIntOrNull() in 150..193
                        value.endsWith("in") -> value.dropLast(2).toIntOrNull() in 59..76
                        else -> false
                    }
                    "hcl" -> value.startsWith('#') && value.drop(1).let { color ->
                        color.length == 6 && color.all { colorChar -> colorChar.isDigit() || colorChar in 'a'..'f' }
                    }
                    "ecl" -> value in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                    "pid" -> value.length == 9 && value.all { it.isDigit() }
                    "cid" -> true
                    else -> throw IllegalStateException()
                }
            }
        }
    }
}