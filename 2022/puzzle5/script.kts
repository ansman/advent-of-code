#!/usr/bin/env kotlin

val Char.priority: Int
    get() = when {
        this in 'a'..'z' -> (this - 'a').toInt() + 1
        this in 'A'..'Z' -> (this - 'A').toInt() + 27
        else -> error("Unknown item $this")
    }

var score = 0L
while (true) {
    val line = readLine() ?: break
    val left = line.take(line.length / 2).toCharArray().toSet()
    val right  = line.drop(line.length / 2).toCharArray().toSet()
    score += left.intersect(right).sumOf { it.priority }
}
println(score)