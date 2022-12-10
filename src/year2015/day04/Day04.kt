package year2015.day04

import utils.Solution
import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    with(Day04()) {
        test(::part1, 609043, "abcdef")
        test(::part1, 1048970, "pqrstuv")
        solve()
    }
}

class Day04 : Solution() {
    override val day = 4
    override val year = 2015

    companion object {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
    }

    override fun part1(): Any {
        val secret = input.first()
        var answer = 1

        while (true) {
            val hash = md5("$secret$answer")
            if (hash.length <= 27) break
            answer += 1
        }

        return answer
    }

    override fun part2(): Any {
        val secret = input.first()
        var answer = 1

        while (true) {
            val hash = md5("$secret$answer")
            if (hash.length <= 26) break
            answer += 1
        }

        return answer
    }

    // .padStart(32, '0')
    private fun md5(input: String) = BigInteger(1, md.digest(input.toByteArray())).toString(16)
}