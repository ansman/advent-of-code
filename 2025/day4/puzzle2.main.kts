#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val grid = generateSequence(::readlnOrNull)
    .map { it.toMutableList() }
    .toMutableList()

var removed = 0
var didRemove = true
while (didRemove) {
    didRemove = false
    grid.indices
        .forEach { y ->
            grid[0].indices
                .filter { grid[y][it] == '@' }
                .forEach { x ->
                    val rolls = ((y - 1)..(y + 1))
                        .flatMap { yp ->
                            ((x - 1)..(x + 1)).filter { xp ->
                                grid.getOrNull(yp)?.getOrNull(xp) == '@'
                            }
                        }
                        .count()
                    if (rolls <= 4) {
                        grid[y][x] = 'x'
                        didRemove = true
                        ++removed
                    }
                }
        }
}
println(removed)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")