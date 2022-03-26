package year2015.day12

import utils.Solution

fun main() {
    with(Day12()) {
        test(::part1, 6, "[1,2,3]")
        test(::part1, 6, "{\"a\":2,\"b\":4}")
        test(::part1, 3, "[[[3]]]")
        test(::part1, 3, "{\"a\":{\"b\":4},\"c\":-1}")
        test(::part1, 0, "{\"a\":[-1,1]}")
        test(::part1, 0, "[-1,{\"a\":1}]")
        test(::part1, 0, "[]")
        test(::part1, 0, "{}")
        test(::part2, 6, "[1,2,3]")
        test(::part2, 4, "[1,{\"c\":\"red\",\"b\":2},3]")
        test(::part2, 0, "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}")
        test(::part2, 6, "[1,\"red\",5]")
        test(::part2, 15, "{\"f\":5,\"e\":{\"a\":1,\"b\":[\"red\"],\"h\":5},\"g\":4}")
        test(::part2, 9, "{\"f\":5,\"e\":{\"a\":1,\"b\":\"red\",\"h\":5},\"g\":4}")
        solve()
    }
}

private class Day12 : Solution {
    override val day = 12
    override val year = 2015

    override fun part1(input: List<String>): Any {
        val s = input.first()
        var total = 0
        var i = 0

        while (i < s.length) {
            if (s[i].isDigit() || s[i] == '-') {
                // Found start of digit
                val start = i
                do {
                    i += 1
                } while (i < s.length && s[i].isDigit())
                // Found end of digit, parse and add
                total += s.substring(start, i).toInt()
            } else {
                i += 1
            }
        }

        return total
    }

    // This would have been way easier by parsing it as a tree first but this is "one pass"
    override fun part2(input: List<String>): Any {
        val s = input.first()
        val tokens = ArrayDeque<Token>()
        var i = 0
        var total = 0

        while (i < s.length) {
            val c = s[i]

            when {
                c == '{' -> tokens.addLast(Token.StartObjectToken)
                c == '}' -> tokens.addLast(Token.EndObjectToken)
                c == '-' || c.isDigit() -> {
                    // Found start of digit
                    val start = i
                    while (s[i + 1].isDigit()) {
                        i += 1
                    }
                    // Found end of digit, parse and add
                    val number = s.substring(start..i).toInt()
                    total += number
                    tokens.addLast(Token.NumberToken(number))
                }
                c == ':' && i + 5 < s.lastIndex && s.substring(i + 2..i + 4) == "red" -> {
                    // Found a red property
                    var openedScopes = 0

                    while (true) {
                        when (val token = tokens.removeLast()) {
                            is Token.NumberToken -> total -= token.value
                            is Token.StartObjectToken -> {
                                if (openedScopes == 0) {
                                    // got to the start of the object
                                    // keep going until this object scope ends
                                    i += 5
                                    while (s[i] != '}' || openedScopes != 0) {
                                        when {
                                            s[i] == '{' -> openedScopes += 1
                                            s[i] == '}' -> openedScopes -= 1
                                        }
                                        i += 1
                                    }
                                    break
                                } else {
                                    openedScopes -= 1
                                }
                            }
                            is Token.EndObjectToken -> openedScopes += 1
                        }
                    }
                }
            }
            i += 1
        }

        return total
    }

    sealed class Token {
        data class NumberToken(val value: Int) : Token()
        object StartObjectToken : Token()
        object EndObjectToken : Token()
    }
}