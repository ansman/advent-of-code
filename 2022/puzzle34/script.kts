#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min

val chamberWidth = 7
val topPerColumn = LongArray(chamberWidth) { -1L }
val gusts = readLine()!!
var top = 0L
var gustIndex = 0L
var rockIndex = 0L

fun nextGust() = gusts[(gustIndex++ % gustsSize).toInt()]

val grid = mutableListOf<CharArray>()
fun getLine(i: Long): CharArray {
    while (i >= grid.size) {
        grid.add(CharArray(chamberWidth) { ' ' })
    }
    return grid[i.toInt()]
}

val rocks = listOf(
        arrayOf(
                arrayOf('@', '@', '@', '@')
        ),
        arrayOf(
                arrayOf(' ', '@', ' '),
                arrayOf('@', '@', '@'),
                arrayOf(' ', '@', ' '),
        ),
        arrayOf(
                arrayOf('@', '@', '@'),
                arrayOf(' ', ' ', '@'),
                arrayOf(' ', ' ', '@'),
        ),
        arrayOf(
                arrayOf('@'),
                arrayOf('@'),
                arrayOf('@'),
                arrayOf('@'),
        ),
        arrayOf(
                arrayOf('@', '@'),
                arrayOf('@', '@'),
        ),
)
val rocksSize = rocks.size.toLong()
val gustsSize = gusts.length.toLong()

data class SimulationResult(
        val baseHeight: Long,
        val baseIterations: Long,
        val repeatingHeights: List<Long>,
        val repeatingHeight: Long,
) {
    val repeatingCount: Int get() = repeatingHeights.size
}

fun runFullSimulation(): SimulationResult {
    var runs = 0L
    val seen = mutableSetOf<String>()
    val heights = mutableListOf<Long>()
    val states = mutableListOf<String>()
    while (true) {
        heights.add(top)
        val rock = rocks[(rockIndex++ % rocksSize).toInt()]
        var rockLeft = 2
        var rockBottom = top + 3L
        fun rockTop() = rockBottom + rock.size - 1
        fun rockRight() = rockLeft + rock[0].size - 1

        fun canMove(dx: Int, dy: Long): Boolean {
            if (rockBottom + dy < 0) return false
            rock.forEachIndexed { ry, line ->
                line.forEachIndexed { rx, r ->
                    if (r == ' ') {
                        return@forEachIndexed
                    }
                    if (getLine(ry + rockBottom + dy).getOrNull(rx + rockLeft + dx) != ' ') {
                        return false
                    }
                }
            }
            return true
        }

        fun copyRockToGrid(): Long {
            rock.forEachIndexed { ry, line ->
                line.forEachIndexed { rx, c ->
                    if (c == '@') {
                        val y = ry + rockBottom
                        val x = rx + rockLeft
                        getLine(y)[rx + rockLeft] = '#'
                        topPerColumn[x] = max(topPerColumn[x], y)
                    }
                }
            }
            return rockTop() + 1
        }

        while (true) {
            val gust = nextGust()
            val dx = when (gust) {
                '>' -> 1
                '<' -> -1
                else -> error("Unknown gust $gust")
            }
            if (canMove(dx, 0)) {
                rockLeft += dx
            }
            if (canMove(0, -1)) {
                rockBottom -= 1
            } else {
                top = max(top, copyRockToGrid())
                break
            }
        }
        ++runs

        val state = "%d:%d:%s".format(rockIndex % rocksSize, gustIndex % gustsSize, topPerColumn.joinToString(":") { (top - it - 1).toString() })
        states.add(state)
        if (!seen.add(state)) {
            val baseIterations = states.indexOfFirst { it == state }
            val topAtBaseEnd = heights[baseIterations]
            val repeatingCount = heights.size - baseIterations - 1
            val topAtRepeatStart = heights[baseIterations]
            val repeatingHeights = heights.drop(baseIterations).map { it - topAtRepeatStart }
            return SimulationResult(
                    baseHeight = topAtRepeatStart,
                    baseIterations = baseIterations.toLong(),
                    repeatingHeights = repeatingHeights.take(repeatingCount),
                    repeatingHeight = repeatingHeights.last()
            )
        }
    }
}

val result = runFullSimulation()
fun SimulationResult.computeHeight(iterations: Long): Long {
    val wholeTowerIterations = (iterations - result.baseIterations) / repeatingCount
    var height = baseHeight + repeatingHeight * wholeTowerIterations
    val extraIterations = (iterations - result.baseIterations) % repeatingCount
    val extraHeight = result.repeatingHeights[extraIterations.toInt()]
    return height + extraHeight
}

println(result.computeHeight(2022L))
println(result.computeHeight(1000000000000L))