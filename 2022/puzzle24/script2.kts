#!/usr/bin/env kotlin

data class Point(val x: Int, val y: Int) {
    fun increment(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)
}

lateinit var end: Point
val grid = mutableListOf<IntArray>()

while (true) {
    val line = readLine() ?: break
    val y = grid.size
    val row = line.toCharArray()
            .asSequence()
            .mapIndexed { x, c ->
                when (c) {
                    'S' -> 'a'

                    'E' -> {
                        end = Point(x, y)
                        'z'
                    }

                    else -> c
                }
            }
            .map { it.code - 'a'.code }
            .toList()
            .toIntArray()
    grid.add(row)
}

val height = grid.size
val width = grid[0].size

val queue = ArrayDeque<Point>()
var steps = Array(height) {
    IntArray(width) { -1 }
}

fun Point.enqueueNextPoint(dx: Int = 0, dy: Int = 0) {
    val nextX = x + dx
    val nextY = y + dy
    if (
            nextX !in 0..width-1 ||
            nextY !in 0..height-1 ||
            grid[nextY][nextX] < grid[y][x] - 1
    ) {
        return
    }
    if (steps[nextY][nextX] != -1) {
        return
    }

    queue.add(Point(nextX, nextY))
    steps[nextY][nextX] = steps[y][x] + 1
}

queue.add(end)
steps[end.y][end.x] = 0

while (queue.isNotEmpty()) {
    val p = queue.removeFirst()
    if (grid[p.y][p.x] == 0) {
        println(steps[p.y][p.x])
        break
    }
    p.enqueueNextPoint(-1, 0)
    p.enqueueNextPoint(1, 0)
    p.enqueueNextPoint(0, -1)
    p.enqueueNextPoint(0, 1)
}