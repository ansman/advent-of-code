#!/usr/bin/env kotlin

import java.math.BigInteger

// Evaluates the worksheet when numbers are read column-wise right-to-left.
val rawLines = generateSequence(::readlnOrNull).toList()
require(rawLines.isNotEmpty()) { "Empty input" }

val width = rawLines.maxOf { it.length }
val lines = rawLines.map { it.padEnd(width, ' ') }
val lastRow = lines.lastIndex

// Determine occupied columns (any non-space).
val occupied = BooleanArray(width) { c -> lines.any { it[c] != ' ' } }

// Build contiguous occupied ranges = individual problems.
val ranges = mutableListOf<Pair<Int, Int>>()
var col = 0
while (col < width) {
    if (!occupied[col]) {
        col++
        continue
    }
    val start = col
    while (col < width && occupied[col]) col++
    ranges += start to col - 1
}

val opsLine = lines[lastRow]
var total = BigInteger.ZERO

for ((start, end) in ranges) {
    val op = opsLine.substring(start, end + 1).firstOrNull { it == '+' || it == '*' }
        ?: error("Missing operator in columns $start..$end")

    val numbers = buildList {
        for (c in end downTo start) {
            val digits = StringBuilder()
            for (r in 0 until lastRow) {
                val ch = lines[r][c]
                if (ch.isDigit()) digits.append(ch)
            }
            if (digits.isNotEmpty()) add(digits.toString().toBigInteger())
        }
    }
    require(numbers.isNotEmpty()) { "No numbers found in columns $start..$end" }

    val value = when (op) {
        '+' -> numbers.fold(BigInteger.ZERO, BigInteger::add)
        '*' -> numbers.fold(BigInteger.ONE, BigInteger::multiply)
        else -> error("Unknown operator $op")
    }
    total += value
}

println(total)
