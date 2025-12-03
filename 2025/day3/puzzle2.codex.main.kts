#!/usr/bin/env kotlin

// Picks the lexicographically largest subsequence of length 12 from each bank
// (digits must stay in order), then sums all resulting 12-digit numbers.

import java.math.BigInteger

private fun maxJoltage(bank: String, take: Int = 12): BigInteger {
    require(take <= bank.length) { "Bank too short for requested length" }

    val n = bank.length
    var toRemove = n - take
    val stack = ArrayList<Char>(take)

    for (i in bank.indices) {
        val ch = bank[i]

        while (
            toRemove > 0 &&
            stack.isNotEmpty() &&
            stack.last() < ch &&
            (stack.size - 1 + (n - i) >= take)
        ) {
            stack.removeAt(stack.lastIndex)
            toRemove--
        }

        stack.add(ch)
    }

    // Drop any remaining excess digits from the end to reach exact length.
    while (toRemove > 0) {
        stack.removeAt(stack.lastIndex)
        toRemove--
    }

    val chosen = buildString(take) { stack.take(take).forEach { append(it) } }
    return BigInteger(chosen)
}

val total = generateSequence { readlnOrNull() }
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .map { maxJoltage(it) }
    .fold(BigInteger.ZERO, BigInteger::add)

println(total)
