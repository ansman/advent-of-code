#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min

val chamberWidth = 7
val grid = mutableListOf<CharArray>()
fun getLine(i: Int): CharArray {
    while (i >= grid.size) {
        grid.add(CharArray(chamberWidth) { ' ' })
    }
    return grid[i]
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

val gusts = readLine()!!
var top = 0
var gustIndex = 0

fun nextGust() = gusts[gustIndex++ % gusts.length]
val printMoves = false
val printGrid = false

repeat(2022) { i ->
    val rock = rocks[i % rocks.size]
    var rockLeft = 2
    var rockBottom = top + 3
    fun rockTop() = rockBottom + rock.size - 1
    fun rockRight() = rockLeft + rock[0].size - 1
    fun drawGrid() {
        println("Iteration $i")
        for (y in rockTop() downTo max(0, rockTop()-15)) {
            print('|')
            val line = getLine(y)
            repeat(chamberWidth) { x ->
                val c = rock.getOrNull(y - rockBottom)?.getOrNull(x - rockLeft)
                        ?: line[x]
                print(if (c == ' ') '.' else c)
            }
            println('|')
        }
        print('+')
        repeat(chamberWidth) {
            print('-')
        }
        println('+')
        println()
    }

    fun canMove(dx: Int, dy: Int): Boolean {
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

    fun copyRockToGrid(): Int {
        rock.forEachIndexed { ry, line ->
            line.forEachIndexed { rx, c ->
                if (c == '@') {
                    getLine(ry + rockBottom)[rx + rockLeft] = '#'
                }
            }
        }
        return rockTop() + 1
    }

    if (printGrid && i < 100) drawGrid()

    while (true) {
        val gust = nextGust()
        val dx = when (gust) {
            '>' -> 1
            '<' -> -1
            else -> error("Unknown gust $gust")
        }
        if (canMove(dx, 0)) {
            if (printMoves) println("Moving rock $gust, gustIndex is now $gustIndex")
            rockLeft += dx
        } else {
            if (printMoves) println("Rock cannot move dx=$dx")
        }
        if (printMoves && printGrid) drawGrid()
        if (canMove(0, -1)) {
            if (printMoves) println("Rock moved down")
            rockBottom -= 1
        } else {
            if (printMoves) println("Rock cannot move down")
            top = max(top, copyRockToGrid())
            break
        }
        if (printMoves && printGrid) drawGrid()
    }
}
println("Top is $top")