#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val grid = generateSequence(::readlnOrNull).toList()

val accessibleRolls = grid.indices
    .flatMap { y ->
        grid[0].indices
            .filter { grid[y][it] == '@' }
            .filter { x ->
                val rolls = ((y - 1)..(y + 1))
                    .flatMap { yp ->
                        ((x - 1)..(x + 1)).filter { xp ->
                            grid.getOrNull(yp)?.getOrNull(xp) == '@'
                        }
                    }
                    .count()
                rolls <= 4
            }
    }
    .count()
println(accessibleRolls)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")