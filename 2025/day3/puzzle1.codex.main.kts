#!/usr/bin/env kotlin

// For each bank of batteries (one line), pick two digits in order that form the
// largest possible two-digit joltage. Scan from right to left while keeping the
// best digit seen so far to form a pair in O(n) per line.

fun maxJoltageForBank(bank: String): Int {
    var bestPair = -1
    var bestRightDigit = -1

    for (i in bank.lastIndex downTo 0) {
        val digit = bank[i] - '0'

        if (bestRightDigit != -1) {
            bestPair = maxOf(bestPair, digit * 10 + bestRightDigit)
        }

        if (digit > bestRightDigit) {
            bestRightDigit = digit
        }
    }

    return bestPair
}

val total = generateSequence { readlnOrNull() }
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .sumOf { maxJoltageForBank(it) }

println(total)
