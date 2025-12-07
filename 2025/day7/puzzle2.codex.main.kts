#!/usr/bin/env kotlin

import java.io.File
import java.math.BigInteger
import kotlin.time.TimeSource

fun countTimelines(lines: List<String>): BigInteger {
    val startRow = lines.indexOfFirst { 'S' in it }
    require(startRow >= 0) { "No start position found" }
    val startCol = lines[startRow].indexOf('S')

    var active = mutableMapOf(startCol to BigInteger.ONE)
    var finished = BigInteger.ZERO

    fun addNext(targetCol: Int, row: String, count: BigInteger, next: MutableMap<Int, BigInteger>) {
        if (targetCol !in row.indices) {
            finished += count
        } else {
            next[targetCol] = next.getOrDefault(targetCol, BigInteger.ZERO) + count
        }
    }

    for (rowIndex in startRow + 1 until lines.size) {
        val row = lines[rowIndex]
        val next = mutableMapOf<Int, BigInteger>()

        for ((col, count) in active) {
            if (col !in row.indices) {
                finished += count
                continue
            }

            if (row[col] == '^') {
                addNext(col - 1, row, count, next)
                addNext(col + 1, row, count, next)
            } else {
                addNext(col, row, count, next)
            }
        }

        active = next
    }

    finished += active.values.fold(BigInteger.ZERO, BigInteger::add)
    return finished
}

val timer = TimeSource.Monotonic.markNow()
val inputFile = if (args.isNotEmpty()) args[0] else "input.txt"
val lines = File(inputFile).readLines()

val result = countTimelines(lines)
println(result)
println("Ran in ${timer.elapsedNow().inWholeMilliseconds}ms")
