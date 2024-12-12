#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: Any?) {
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

class FenceMap {
    private var vertical = mutableMapOf<Float, MutableSet<Int>>()
    private var horizontal = mutableMapOf<Float, MutableSet<Int>>()

    fun addHorizontal(x: Int, y: Float) {
        horizontal.getOrPut(y, ::mutableSetOf).add(x)
    }

    fun addVertical(x: Float, y: Int) {
        vertical.getOrPut(x, ::mutableSetOf).add(y)
    }


    fun countSides(): Int = vertical.countSections() + horizontal.countSections()

    private fun MutableMap<*, MutableSet<Int>>.countSections(): Int {
        debug(this)
        var sides = 0
        for ((_, sections) in this) {
            var last = Int.MIN_VALUE
            for (section in sections.toMutableList().apply { sort() }) {
                if (section != last + 1 && last != Int.MIN_VALUE) {
                    ++sides
                }
                last = section
            }
            ++sides
        }
        debug("Sides: $sides")
        return sides
    }
}

fun computeFencePrice(x: Int, y: Int): Long {
    val fenceMap = FenceMap()
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
            fenceMap.addVertical(x - 0.1f, y)
        }
        if (c == get(x + 1, y)) {
            dfs(x + 1, y)
        } else {
            fenceMap.addVertical(x + 0.1f, y)
        }
        if (c == get(x, y - 1)) {
            dfs(x, y - 1)
        } else {
            fenceMap.addHorizontal(x, y - 0.1f)
        }
        if (c == get(x, y + 1)) {
            dfs(x, y + 1)
        } else {
            fenceMap.addHorizontal(x, y + 0.1f)
        }
    }

    dfs(x, y)
    debug("Section ${get(x, y)} has ${fenceMap.countSides()} sides")
    return area * fenceMap.countSides()
}

var totalPrice = 0L
repeat(w) { x ->
    repeat(h) { y ->
        if (x to y !in visited) {
            totalPrice += computeFencePrice(x, y)
        }
    }
}


println("Total fence price is $totalPrice")
println("Ran in ${start.elapsedNow()}")