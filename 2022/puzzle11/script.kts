#!/usr/bin/env kotlin

val line = readLine()!!

var i = 0
while (i < line.length - 4) {
    if (line.subSequence(i, i + 4).chars().distinct().count() == 4L) {
        println(i + 4)
        break
    }
    ++i
}