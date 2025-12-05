#!/usr/bin/env kotlin

// Counts how many available ingredient IDs fall into any inclusive fresh range.

val lines = generateSequence(::readlnOrNull).toList()
val splitIndex = lines.indexOfFirst { it.isBlank() }

val rangeLines = if (splitIndex == -1) lines else lines.take(splitIndex)
val idLines = if (splitIndex == -1) emptyList() else lines.drop(splitIndex + 1)

val ranges = rangeLines
    .filter { it.isNotBlank() }
    .map { line ->
        val (start, end) = line.trim().split('-', limit = 2)
        start.toLong() to end.toLong()
    }

require(ranges.isNotEmpty()) { "No fresh ranges provided" }

// Merge overlapping or adjacent ranges to speed up membership checks.
val merged = mutableListOf<Pair<Long, Long>>()
for ((start, end) in ranges.sortedBy { it.first }) {
    require(start <= end) { "Invalid range $start-$end" }
    if (merged.isEmpty() || start > merged.last().second + 1) {
        merged.add(start to end)
    } else {
        val last = merged.last()
        merged[merged.lastIndex] = last.first to maxOf(last.second, end)
    }
}

val starts = merged.map { it.first }

var freshCount = 0L
for (line in idLines) {
    if (line.isBlank()) continue
    val id = line.trim().toLong()
    val pos = starts.binarySearch(id)
    val idx = if (pos >= 0) pos else -pos - 2
    if (idx >= 0 && id <= merged[idx].second) freshCount++
}

println(freshCount)
