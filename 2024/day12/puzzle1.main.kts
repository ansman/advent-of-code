#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()

val map = generateSequence { readlnOrNull() }.toList()
val visited = mutableSetOf<Pair<Int, Int>>()
val w = map[0].length
val h = map.size

fun get(x: Int, y: Int) = map.getOrNull(y)?.getOrNull(x)

fun computeFencePrice(x: Int, y: Int): Long {
    var fence = 0L
    var area = 0L
    fun dfs(x: Int, y: Int) {
        val c = get(x, y) ?: return
        if (!visited.add(x to y)) {
            return
        }

        ++area
        if (c == get(x - 1, y)) {
            dfs(x - 1, y)
        } else {
            ++fence
        }
        if (c == get(x + 1, y)) {
            dfs(x + 1, y)
        } else {
            ++fence
        }
        if (c == get(x, y - 1)) {
            dfs(x, y - 1)
        } else {
            ++fence
        }
        if (c == get(x, y + 1)) {
            dfs(x, y + 1)
        } else {
            ++fence
        }
    }

    dfs(x, y)
    return area * fence
}

var totalPrice = 0L
repeat(w) { x ->
    repeat(h) { y ->
        if (x to y in visited) {
            return@repeat
        }
        totalPrice += computeFencePrice(x, y)
    }
}


println("Total fence price is $totalPrice")
println("Ran in ${start.elapsedNow()}")