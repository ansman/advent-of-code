#!/usr/bin/env kotlin

import java.math.BigInteger

// Parses the columnar worksheet, evaluates each vertical problem, and totals their results.
val rawLines = generateSequence(::readlnOrNull).toList()
require(rawLines.isNotEmpty()) { "Empty input" }

val width = rawLines.maxOf { it.length }
val lines = rawLines.map { it.padEnd(width, ' ') }
val lastRow = lines.lastIndex

// Mark columns that contain any non-space character.
val occupied = BooleanArray(width) { col -> lines.any { it[col] != ' ' } }

// Find contiguous column ranges of occupied cells (each represents one problem).
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
    val numbers = buildList {
        for (row in 0 until lastRow) {
            val chunk = lines[row].substring(start, end + 1).trim()
            if (chunk.isNotEmpty()) add(chunk.toBigInteger())
        }
    }
    require(numbers.isNotEmpty()) { "No numbers found in columns $start..$end" }

    val op = opsLine.substring(start, end + 1).firstOrNull { it == '+' || it == '*' }
        ?: error("Missing operator in columns $start..$end")

    val value = when (op) {
        '+' -> numbers.fold(BigInteger.ZERO, BigInteger::add)
        '*' -> numbers.fold(BigInteger.ONE, BigInteger::multiply)
        else -> error("Unknown operator $op")
    }
    total += value
}

println(total)
