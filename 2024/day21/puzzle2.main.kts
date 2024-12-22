#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

data class XY(val x: Int, val y: Int)

fun List<String>.index(): Map<Char, XY> = flatMapIndexed { y, l ->
    l.mapIndexed { x, c -> c to XY(x, y) }
}.associateBy({ it.first }, { it.second })

val numPad = listOf(
    "789",
    "456",
    "123",
    " 0A",
).index()

data class CostMap(val map: Map<Char, Map<Char, Long>>) {
    operator fun get(from: Char, to: Char): Long = map[from]!![to]!!

    fun addRobot(): CostMap = CostMap(
        mapOf(
            'A' to mapOf(
                'A' to computeCost(""),
                '^' to computeCost("<"),
                '>' to computeCost("v"),
                'v' to minOf(computeCost("v<"), computeCost("<v")),
                '<' to minOf(computeCost("v<<"), computeCost("<v<")),
            ),
            '^' to mapOf(
                'A' to computeCost(">"),
                '^' to computeCost(""),
                '>' to minOf(computeCost(">v"), computeCost("v>")),
                'v' to computeCost("v"),
                '<' to computeCost("v<"),
            ),
            '<' to mapOf(
                'A' to minOf(computeCost(">>^"), computeCost(">^>")),
                '^' to computeCost(">^"),
                '>' to computeCost(">>"),
                'v' to computeCost(">"),
                '<' to computeCost(""),
            ),
            'v' to mapOf(
                'A' to minOf(computeCost(">^"), computeCost("^>")),
                '^' to computeCost("^"),
                '>' to computeCost(">"),
                'v' to computeCost(""),
                '<' to computeCost("<"),
            ),
            '>' to mapOf(
                'A' to computeCost("^"),
                '^' to minOf(computeCost("^<"), computeCost("<^")),
                '>' to computeCost(""),
                'v' to computeCost("<"),
                '<' to computeCost("<<"),
            ),
        )
    )

    private fun computeCost(keys: String): Long {
        var currentKey = 'A'
        var cost = 0L
        keys.forEach { key ->
            cost += this[currentKey, key] + 1
            currentKey = key
        }
        return cost + this[currentKey, 'A']
    }
}

val baseCost = CostMap(
    mapOf(
        'A' to mapOf('A' to 0L, '^' to 0L, '>' to 0L, 'v' to 0L, '<' to 0L),
        '^' to mapOf('A' to 0L, '^' to 0L, '>' to 0L, 'v' to 0L, '<' to 0L),
        '>' to mapOf('A' to 0L, '^' to 0L, '>' to 0L, 'v' to 0L, '<' to 0L),
        'v' to mapOf('A' to 0L, '^' to 0L, '>' to 0L, 'v' to 0L, '<' to 0L),
        '<' to mapOf('A' to 0L, '^' to 0L, '>' to 0L, 'v' to 0L, '<' to 0L),
    )
)

fun costToMove(
    fromChar: Char,
    toChar: Char,
    costs: CostMap,
    robotsLeft: Int,
): Long {
    if (robotsLeft > 1) {
        return costToMove(fromChar, toChar, costs.addRobot(), robotsLeft - 1)
    }

    val from = numPad[fromChar]!!
    val to = numPad[toChar]!!

    val distanceX = (from.x - to.x).absoluteValue
    val distanceY = (from.y - to.y).absoluteValue
    val dx = if (to.x < from.x) '<' else '>'
    val dy = if (to.y < from.y) '^' else 'v'

    fun horizontalFirst(): Long = costs['A', dx] + distanceX + costs[dx, dy] + distanceY + costs[dy, 'A'] + 1
    fun verticalFirst(): Long = costs['A', dy] + distanceY + costs[dy, dx] + distanceX + costs[dx, 'A'] + 1

    return if (distanceY == 0 && distanceX == 0) {
        1 // Just press A
    } else if (distanceY == 0) {
        // Move arm to dx, press the required number of times, then move back to A and press it
        costs['A', dx] + distanceX + costs[dx, 'A'] + 1
    } else if (distanceX == 0) {
        // Move arm to dy, press the number of times, then move back to A and press it
        costs['A', dy] + distanceY + costs[dy, 'A'] + 1
    } else {
        val fromLeftColumn = from.x == 0
        val toLeftColumn = to.x == 0
        val fromBottomRow = from.y == 3
        val toBottomRow = to.y == 3
        val fromOrToLeftColumn = fromLeftColumn || toLeftColumn
        val fromOrToBottomRow = fromBottomRow || toBottomRow
        if (!fromOrToLeftColumn || !fromOrToBottomRow) {
            min(horizontalFirst(), verticalFirst())
        } else if (toBottomRow) {
            horizontalFirst()
        } else {
            verticalFirst()
        }
    }
}

fun codeCost(code: String): Long = "A$code".zipWithNext().sumOf { (from, to) ->
    costToMove(from, to, baseCost, robots)
}

val robots = args.getOrNull(0)?.toIntOrNull() ?: 26
val combinedCosts = generateSequence { readlnOrNull() }
    .map { code ->
        val codeValue = code.removeSuffix("A").toInt()
        val codeCost = codeCost(code)
        println("$code = $codeCost * $codeValue")
        codeValue * codeCost
    }
    .sum()

println("Combined costs: $combinedCosts")
println("Ran in ${startTime.elapsedNow()}")