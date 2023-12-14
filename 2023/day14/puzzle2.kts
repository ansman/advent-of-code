#!/usr/bin/env kotlin

import kotlin.system.exitProcess
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun List<CharArray>.computeLoad(): Int {
    var value = 0
    for (column in this[0].indices) {
        for (row in indices) {
            when (this[row][column]) {
                'O' -> value += size - row
                '.' -> continue
                '#' -> continue
                else -> error("Unknown char ${this[row][column]}")
            }
        }
    }
    return value
}

fun List<CharArray>.roll(dx: Int, dy: Int) {
    fun doMove(r: Int, c: Int) {
        if (this[r][c] != 'O') return
        var nr = r + dy
        var nc = c + dx
        while (true) {
            if (nr < 0 || nr > lastIndex || nc < 0 || nc > this[0].lastIndex) return
            if (this[nr][nc] == '.') {
                this[nr - dy][nc - dx] = '.'
                this[nr][nc] = 'O'
            } else {
                return
            }
            nr += dy
            nc += dx
        }
    }

    if (dy != 0) {
        for (c in this[0].indices) {
            for (r in indices.let { if (dy > 0) it.reversed() else it }) {
                doMove(r, c)
            }
        }
    } else {
        for (r in indices) {
            for (c in this[r].indices.let { if (dx > 0) it.reversed() else it }) {
                doMove(r, c)
            }
        }
    }
}

val grid = generateSequence(::readlnOrNull)
        .map { it.toCharArray() }
        .toList()

val directions = listOf(
        0 to -1, // N
        -1 to 0, // W
        0 to 1,  // S
        1 to 0   // E
)

var seen = mutableSetOf<List<String>>()
var iteration = 0
var needsLoop = true
val totalLoops = 1_000_000_000
while (iteration < totalLoops) {
    for ((x, y) in directions) {
        grid.roll(x, y)
    }
    if (needsLoop) {
        val snapshot = grid.map { String(it) }
        if (!seen.add(grid.map { String(it) })) {
            val loopLength = iteration - seen.toList().indexOf(snapshot)
            iteration += ((totalLoops - iteration) / loopLength) * loopLength
            needsLoop = false
        }
    }
    ++iteration
}
println(grid.computeLoad())

println(start.elapsedNow())