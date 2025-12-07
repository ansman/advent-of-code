#!/usr/bin/env kotlin

import java.io.File
import kotlin.time.TimeSource

fun countSplits(lines: List<String>): Long {
    val startRow = lines.indexOfFirst { 'S' in it }
    require(startRow >= 0) { "No start position found" }
    val startCol = lines[startRow].indexOf('S')

    var beams = mutableSetOf(startCol)
    var splits = 0L

    for (rowIndex in startRow + 1 until lines.size) {
        val row = lines[rowIndex]
        val next = mutableSetOf<Int>()

        for (col in beams) {
            if (col !in row.indices) continue
            if (row[col] == '^') {
                splits++
                if (col - 1 >= 0) next.add(col - 1)
                if (col + 1 < row.length) next.add(col + 1)
            } else {
                next.add(col)
            }
        }

        beams = next
        if (beams.isEmpty()) break
    }

    return splits
}

val timer = TimeSource.Monotonic.markNow()
val inputFile = if (args.isNotEmpty()) args[0] else "input.txt"
val lines = File(inputFile).readLines()

val result = countSplits(lines)
println(result)
println("Ran in ${timer.elapsedNow().inWholeMilliseconds}ms")
