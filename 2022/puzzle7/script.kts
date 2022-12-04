#!/usr/bin/env kotlin

fun IntRange.contains(other: IntRange): Boolean =
        other.first >= first && other.last <= last

var overlaps = 0
while (true) {
    val (first, second) = readLine()
            ?.split(",")
            ?.map {
                val (start, end) = it.split("-").map(String::toInt)
                start..end
            }
            ?: break

    if (first.contains(second) || second.contains(first)) {
        ++overlaps
    }
}
println(overlaps)