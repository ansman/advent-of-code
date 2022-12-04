#!/usr/bin/env kotlin

var overlaps = 0
while (true) {
    val (first, second) = readLine()
            ?.split(",")
            ?.map {
                val (start, end) = it.split("-").map(String::toInt)
                (start..end).toSet()
            }
            ?: break

    if (first.intersect(second).isNotEmpty()) {
        ++overlaps
    }
}
println(overlaps)