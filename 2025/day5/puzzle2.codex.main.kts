#!/usr/bin/env kotlin

// Totals the count of distinct ingredient IDs covered by any inclusive fresh range.

val lines = generateSequence(::readlnOrNull).toList()
val splitIndex = lines.indexOfFirst { it.isBlank() }
val rangeLines = if (splitIndex == -1) lines else lines.take(splitIndex)

val ranges = rangeLines
    .filter { it.isNotBlank() }
    .map { line ->
        val (start, end) = line.trim().split('-', limit = 2)
        val s = start.toLong()
        val e = end.toLong()
        require(s <= e) { "Invalid range $s-$e" }
        s to e
    }

require(ranges.isNotEmpty()) { "No fresh ranges provided" }

// Merge overlapping or adjacent ranges then sum lengths.
val merged = mutableListOf<Pair<Long, Long>>()
for ((start, end) in ranges.sortedBy { it.first }) {
    if (merged.isEmpty() || start > merged.last().second + 1) {
        merged.add(start to end)
    } else {
        val last = merged.last()
        merged[merged.lastIndex] = last.first to maxOf(last.second, end)
    }
}

val totalFresh = merged.fold(0L) { acc, (s, e) -> acc + (e - s + 1) }
println(totalFresh)
