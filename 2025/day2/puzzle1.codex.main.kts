#!/usr/bin/env kotlin

import java.math.BigInteger

// Sums all IDs that consist of some digit sequence repeated twice and fall within the given ranges.
// Ranges are provided as comma-separated "start-end" pairs on stdin (possibly wrapped across lines).

data class Range(val start: Long, val end: Long)

fun ceilDiv(a: Long, b: Long): Long = (a + b - 1) / b // assumes a, b > 0

fun parseRanges(lines: List<String>): List<Range> = lines
    .flatMap { it.split(',') }
    .mapNotNull { token ->
        val trimmed = token.trim()
        if (trimmed.isEmpty()) return@mapNotNull null
        val parts = trimmed.split('-')
        require(parts.size == 2) { "Bad range token: $trimmed" }
        val start = parts[0].toLong()
        val end = parts[1].toLong()
        require(start <= end) { "Start after end in $trimmed" }
        Range(start, end)
    }

fun mergeRanges(ranges: List<Range>): List<Range> {
    if (ranges.isEmpty()) return emptyList()
    val sorted = ranges.sortedBy { it.start }
    val merged = mutableListOf<Range>()
    var current = sorted.first()
    for (r in sorted.drop(1)) {
        if (r.start <= current.end + 1) {
            current = Range(current.start, maxOf(current.end, r.end))
        } else {
            merged += current
            current = r
        }
    }
    merged += current
    return merged
}

val inputLines = generateSequence { readlnOrNull() }.toList()
val ranges = mergeRanges(parseRanges(inputLines))

if (ranges.isEmpty()) {
    println(0)
} else {
    val maxVal = ranges.maxOf { it.end }
    val maxHalfLen = maxVal.toString().length / 2 // only even-length IDs matter

    val pow10 = LongArray(maxHalfLen + 1)
    pow10[0] = 1L
    for (i in 1..maxHalfLen) pow10[i] = pow10[i - 1] * 10L

    var total = BigInteger.ZERO
    val two = BigInteger.valueOf(2L)

    for (k in 1..maxHalfLen) {
        val base = pow10[k] // 10^k
        val factor = base + 1 // multiplier for repeated numbers: x * (10^k + 1)
        val minX = base / 10 // smallest k-digit number (no leading zero)
        val maxX = base - 1 // largest k-digit number

        for (range in ranges) {
            val xLow = maxOf(minX, ceilDiv(range.start, factor))
            val xHigh = minOf(maxX, range.end / factor)
            if (xLow > xHigh) continue

            val count = xHigh - xLow + 1
            val sumX = BigInteger.valueOf(count)
                .multiply(BigInteger.valueOf(xLow + xHigh))
                .divide(two) // arithmetic series sum
            val sumNumbers = BigInteger.valueOf(factor).multiply(sumX)
            total = total.add(sumNumbers)
        }
    }

    println(total)
}
