#!/usr/bin/env kotlin

import java.util.*
import kotlin.time.TimeSource

val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

data class Coordinate(val x: Int, val y: Int, val direction: Direction) {
    fun step(): Coordinate = copy(x = x + direction.dx, y = y + direction.dy)
    fun turnLeft(): Coordinate = copy(direction = direction.turnLeft())
    fun turnRight(): Coordinate = copy(direction = direction.turnRight())
}

val startTime = TimeSource.Monotonic.markNow()

var startX = 0
var startY = 0
var endX = 0
var endY = 0
val grid = generateSequence { readlnOrNull() }
    .onEachIndexed { y, l ->
        val s = l.indexOf('S')
        val e = l.indexOf('E')
        if (s >= 0) {
            startX = s
            startY = y
        }
        if (e >= 0) {
            endX = e
            endY = y
        }
    }
    .toList()
val w = grid[0].length
val h = grid.size

enum class Direction(val dx: Int = 0, val dy: Int = 0) {
    Up(dy = -1),
    Down(dy = 1),
    Left(dx = -1),
    Right(dx = 1);


    fun turnLeft(): Direction = when (this) {
        Up -> Left
        Down -> Right
        Left -> Down
        Right -> Up
    }

    fun turnRight(): Direction = when (this) {
        Up -> Right
        Right -> Down
        Down -> Left
        Left -> Up
    }
}

//fun computeCost(
//    x: Int,
//    y: Int,
//    dir: Direction,
//    memo: Array<Array<Array<Double>>> = Array(h) { Array(w) { Array(Direction.entries.size) { Double.NaN } } },
//    visited: MutableSet<Step> = mutableSetOf(),
//    indent: String = "",
//): Double {
//    val coordinate = Step(x, y, dir)
//    debug("${indent}Visiting $coordinate")
//
//    if (x < 0 || x >= w || y < 0 || y >= h || grid[y][x] == '#') {
//        debug("${indent}${coordinate} is out of bounds")
//        return Double.POSITIVE_INFINITY
//    }
//    memo[y][x][dir.ordinal].let { cost ->
//        if (!cost.isNaN()) {
//            debug("${indent}$coordinate already visited with cost $cost")
//            return cost
//        }
//    }
//
//    if (grid[y][x] == 'E') {
//        debug("$indent$coordinate is the end")
//        return 0.0
//    }
//
//    if (!visited.add(coordinate)) {
//        debug("$indent$coordinate is already visited")
//        return Double.POSITIVE_INFINITY
//    }
//    return try {
//        min(
//            (1 + computeCost(x + dir.dx, y + dir.dy, dir, memo, visited, indent = "$indent ")),
//            min(
//                (1000 + computeCost(x, y, dir.turnLeft(), memo, visited, indent = "$indent ")),
//                (1000 + computeCost(x, y, dir.turnRight(), memo, visited, indent = "$indent ")),
//            )
//        ).also {
//            debug("$indent$coordinate cost is $it")
//            memo[y][x][dir.ordinal] = it
//        }
//    } finally {
//        visited.remove(coordinate)
//    }
//}

data class Node(val coordinate: Coordinate) {
    var cost = Int.MAX_VALUE
    val neighbors = mutableMapOf<Coordinate, Int>()
}

val nodes = mutableMapOf<Coordinate, Node>()

repeat(h) { y ->
    repeat(w) inner@{ x ->
        val c = grid[y][x]
        if (c == '#') return@inner
        for (dir in Direction.entries) {
            val coordinate = Coordinate(x, y, dir)
            nodes[coordinate] = Node(coordinate)
        }
    }
}

fun Node.addNeighbour(neighbour: Coordinate, cost: Int) {
    if (neighbour in nodes) {
        neighbors[neighbour] = cost
    }
}

fun Node.addNeighbours() {
    addNeighbour(coordinate.step(), 1)
    addNeighbour(coordinate.turnLeft(), 1000)
    addNeighbour(coordinate.turnRight(), 1000)
}

nodes.values.forEach { it.addNeighbours() }

val startNode = nodes.getValue(Coordinate(startX, startY, Direction.Right))
startNode.cost = 0

val visited = mutableSetOf<Node>()
val queue = PriorityQueue<Node>(compareBy { it.cost })
queue.add(startNode)

while (true) {
    val next = queue.poll()
    if (grid[next.coordinate.y][next.coordinate.x] == 'E') {
        break
    }
    if (!visited.add(next)) {
        continue
    }
    for ((neighbour, cost) in next.neighbors) {
        val neighbourNode = nodes.getValue(neighbour)
        val newCost = next.cost + cost
        if (newCost < neighbourNode.cost) {
            neighbourNode.cost = newCost
            queue.remove(neighbourNode)
            queue.add(neighbourNode)
        }
    }
}

//val costsToEnd = Array(h) { Array(w) { IntArray(Direction.entries.size) { Int.MAX_VALUE } } }
//fun computeCost(
//    x: Int,
//    y: Int,
//    dir: Direction,
//    cost: Int,
//    path: List<Step> = listOf(Step(x, y, dir)),
//) {
//    if (x < 0 || x >= w || y < 0 || y >= h || grid[y][x] == '#') {
//        return
//    }
//
//    if (costsToEnd[y][x][dir.ordinal] < cost) {
//        return
//    }
//    costsToEnd[y][x][dir.ordinal] = cost
//    if (grid[y][x] == 'S' && dir == Direction.Right) {
//        debug("Found end ($cost): $path")
//        return
//    }
//    computeCost(
//        x - dir.dx,
//        y - dir.dy,
//        dir,
//        cost + 1,
//        path = if (debug) path + Step(x + dir.dx, y + dir.dy, dir) else path
//    )
//    computeCost(x, y, dir.turnLeft(), cost + 1000, path = if (debug) path + Step(x, y, dir.turnLeft()) else path)
//    computeCost(x, y, dir.turnRight(), cost + 1000, path = if (debug) path + Step(x, y, dir.turnRight()) else path)
//}

val steps = mutableSetOf<Pair<Int, Int>>()
fun computeSteps(
    x: Int,
    y: Int,
    dir: Direction,
    cost: Int,
): Boolean {
    if (x < 0 || x >= w || y < 0 || y >= h || grid[y][x] == '#') {
        return false
    }

    val remainingCost = nodes[Coordinate(x, y, dir)]?.cost ?: return false
    if (cost != remainingCost) {
        return false
    }
    if (grid[y][x] == 'E') {
        steps.add(x to y)
        return true
    }
    val step = computeSteps(x + dir.dx, y + dir.dy, dir, cost + 1)
    val left = computeSteps(x, y, dir.turnLeft(), cost + 1000)
    val right = computeSteps(x, y, dir.turnRight(), cost + 1000)
    if (step || left || right) {
        steps.add(x to y)
        return true
    } else {
        return false
    }
}

val bestCost = Direction.entries.minOf {
    nodes[Coordinate(endX, endY, it)]?.cost ?: Int.MAX_VALUE
}
computeSteps(startX, startY, Direction.Right, cost = 0)
println("Cost to end: $bestCost")
println("Steps: ${steps.size}")

println("Ran in ${startTime.elapsedNow()}")