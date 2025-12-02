#!/usr/bin/env kotlin

import java.math.BigInteger

// Sums all IDs that are made of a digit block repeated at least twice and fall within the provided ranges.

data class Range(val start: Long, val end: Long)

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

fun ceilDiv(a: BigInteger, b: BigInteger): BigInteger {
    // assumes a, b >= 0 and b > 0
    if (a.signum() == 0) return BigInteger.ZERO
    val (q, r) = a.divideAndRemainder(b)
    return if (r.signum() == 0) q else q.add(BigInteger.ONE)
}

val inputLines = generateSequence { readlnOrNull() }.toList()
val ranges = mergeRanges(parseRanges(inputLines))

if (ranges.isEmpty()) {
    println(0)
} else {
    val maxVal = ranges.maxOf { it.end }
    val maxDigits = maxVal.toString().length

    // Precompute powers of 10 up to maxDigits * 2 just in case
    val pow10 = Array(maxDigits + 1) { BigInteger.ONE }
    for (i in 1..maxDigits) pow10[i] = pow10[i - 1].multiply(BigInteger.TEN)

    fun rep(lenSmall: Int, lenBig: Int): BigInteger {
        // 111... pattern to stretch a block of lenSmall digits to lenBig digits (lenSmall | lenBig).
        return pow10[lenBig].subtract(BigInteger.ONE).divide(pow10[lenSmall].subtract(BigInteger.ONE))
    }

    fun divisors(n: Int): List<Int> {
        val divs = mutableListOf<Int>()
        var i = 1
        while (i * i <= n) {
            if (n % i == 0) {
                divs += i
                if (i != n / i) divs += n / i
            }
            i++
        }
        return divs
    }

    // Möbius function up to maxDigits
    val mu = IntArray(maxDigits + 1) { 1 }
    val isPrime = BooleanArray(maxDigits + 1)
    val primes = mutableListOf<Int>()
    for (i in 2..maxDigits) {
        if (!isPrime[i]) {
            primes += i
            mu[i] = -1
        }
        for (p in primes) {
            val v = i * p
            if (v > maxDigits) break
            isPrime[v] = true
            mu[v] = if (i % p == 0) 0 else -mu[i]
            if (i % p == 0) break
        }
    }

    val bigRanges = ranges.map { Pair(BigInteger.valueOf(it.start), BigInteger.valueOf(it.end)) }

    var total = BigInteger.ZERO
    val two = BigInteger.valueOf(2L)

    for (p in 1..maxDigits) { // minimal block length
        val minXPrimitive = pow10[p - 1]
        val maxXPrimitive = pow10[p].subtract(BigInteger.ONE)

        // divisors for inclusion-exclusion
        val divs = divisors(p)

        for (m in 2..(maxDigits / p)) { // repetition count
            val totalDigits = p * m
            if (totalDigits > maxDigits) break

            val overallFactor = pow10[totalDigits].subtract(BigInteger.ONE)
                .divide(pow10[p].subtract(BigInteger.ONE)) // multiplier for full ID

            for ((startBI, endBI) in bigRanges) {
                val xLowRaw = ceilDiv(startBI, overallFactor).max(minXPrimitive)
                val xHighRaw = endBI.divide(overallFactor).min(maxXPrimitive)
                if (xLowRaw > xHighRaw) continue

                // inclusion-exclusion over divisors to keep only primitive x of length p
                var primitiveSumX = BigInteger.ZERO
                for (d in divs) {
                    val muVal = mu[d]
                    if (muVal == 0) continue
                    val q = p / d // smaller block length
                    val stretch = rep(q, p)
                    val yLow = ceilDiv(xLowRaw, stretch).max(pow10[q - 1])
                    val yHigh = xHighRaw.divide(stretch).min(pow10[q].subtract(BigInteger.ONE))
                    if (yLow > yHigh) continue

                    val count = yHigh.subtract(yLow).add(BigInteger.ONE)
                    val sumY = count.multiply(yLow.add(yHigh)).divide(two)
                    val sumX = stretch.multiply(sumY)
                    primitiveSumX = primitiveSumX.add(sumX.multiply(BigInteger.valueOf(muVal.toLong())))
                }

                if (primitiveSumX.signum() != 0) {
                    total = total.add(overallFactor.multiply(primitiveSumX))
                }
            }
        }
    }

    println(total)
}
