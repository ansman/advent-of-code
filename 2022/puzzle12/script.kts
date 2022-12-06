#!/usr/bin/env kotlin

val line = readLine()!!

var i = 0
val markerLength = 14
while (i < line.length - markerLength) {
    if (line.subSequence(i, i + markerLength).chars().distinct().count() == markerLength.toLong()) {
        println(i + markerLength)
        break
    }
    ++i
}