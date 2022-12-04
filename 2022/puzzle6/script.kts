#!/usr/bin/env kotlin

val Char.priority: Int
    get() = when {
        this in 'a'..'z' -> (this - 'a').toInt() + 1
        this in 'A'..'Z' -> (this - 'A').toInt() + 27
        else -> error("Unknown item $this")
    }

var score = 0L
fun processGroup(): Boolean {
    val bags = mutableListOf<Set<Char>>()
    repeat(3) {
        val line = readLine() ?: return false
        bags.add(line.toCharArray().toSet())
    }
    score += bags.reduce { acc, chars -> chars.intersect(acc) }.single().priority
    return true
}
while (processGroup()) {}
println(score)