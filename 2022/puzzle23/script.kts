#!/usr/bin/env kotlin

data class Point(val x: Int, val y: Int) {
    fun increment(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)
}

lateinit var start: Point
lateinit var end: Point
val grid = mutableListOf<IntArray>()
val steps = mutableListOf<IntArray>()

while (true) {
    val line = readLine() ?: break
    val y = grid.size
    val row = line.toCharArray()
            .asSequence()
            .mapIndexed { x, c ->
                when (c) {
                    'S' -> {
                        start = Point(x, y)
                        'a'
                    }

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
    steps.add(IntArray(row.size) { -1 })
}

val height = grid.size
val width = grid[0].size

val queue = ArrayDeque<Point>()

fun Point.enqueueNextPoint(dx: Int = 0, dy: Int = 0) {
    val nextX = x + dx
    val nextY = y + dy
    if (
            nextX !in 0..width-1 ||
            nextY !in 0..height-1 ||
            grid[nextY][nextX] > grid[y][x] + 1
    ) {
        return
    }
    if (steps[nextY][nextX] != -1) {
        return
    }

    queue.add(Point(nextX, nextY))
    steps[nextY][nextX] = steps[y][x] + 1
}

queue.add(start)
steps[start.y][start.x] = 0
while (true) {
    val p = queue.removeFirst()
    if (p == end) {
        println(steps[p.y][p.x])
        break
    }
    p.enqueueNextPoint(-1, 0)
    p.enqueueNextPoint(1, 0)
    p.enqueueNextPoint(0, -1)
    p.enqueueNextPoint(0, 1)
}