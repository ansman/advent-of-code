#!/usr/bin/env kotlin

import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList

val stacks = ArrayList<ArrayDeque<Char>>()
while (true) {
    val line = readLine() ?: break
    if (line[1].isDigit()) break
    var stackIndex = 0
    var i = 1
    while (i < line.length) {
        val stack = stacks.getOrElse(stackIndex) { ArrayDeque<Char>().also(stacks::add) }
        if (line[i] != ' ') {
            stack.addLast(line[i])
        }
        ++stackIndex
        i += 4
    }
}
readLine()
val pattern: Regex = Regex("""move (\d+) from (\d+) to (\d+)""")
while (true) {
    val line = readLine() ?: break
    val (count, from, to) = pattern.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
    repeat(count) {
        stacks[to - 1].addFirst(stacks[from - 1].removeFirst())
    }
}
println(stacks.mapNotNull { it.firstOrNull() }.joinToString(""))