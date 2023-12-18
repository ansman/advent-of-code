#!/usr/bin/env kotlin

import kotlin.math.*
import kotlin.system.exitProcess
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

enum class Direction(val dx: Int, val dy: Int) {
    Up(0, -1),
    Right(1, 0),
    Down(0, 1),
    Left(-1, 0);
}

data class Instruction(
        val direction: Direction,
        val count: Int,
        val color: UInt,
) {
    companion object {
        private val pattern = Regex("""([URDL]) (\d+) \(#([0-9a-fA-F]{6})\)""")
        fun parse(input: String): Instruction {
            val (dir, count, color) = pattern.matchEntire(input)!!.destructured
            return Instruction(Direction.entries.first { it.name.startsWith(dir) }, count.toInt(), color.toUInt(16))
        }
    }
}

fun List<CharArray>.handle(startX: Int, startY: Int, instruction: Instruction) {
    var x = startX
    var y = startY
    repeat(instruction.count) {
        x += instruction.direction.dx
        y += instruction.direction.dy
        this[y][x] = '#'
    }
}

fun List<CharArray>.print() {
    if (!debug) return
    println()
    for (row in this) {
        println(row.joinToString(""))
    }
    println()
}

fun List<CharArray>.isInside(x: Int, y: Int): Boolean {
    var c = this[y][x]
    if (c == '#') return true
    val direction = Direction.entries.minBy {
        when (it) {
            Direction.Up -> y
            Direction.Right -> this[y].size - x
            Direction.Down -> this.size - y
            Direction.Left -> x
        }
    }
    var crossings = 0
    var px = x
    var py = y
    while (true) {
        px += direction.dx
        py += direction.dy
        if (py < 0 || py >= this.size || px < 0 || px >= this[py].size) break
        if (this[py][px] == '#' && c != '#') {
            ++crossings
        }
        c = this[py][px]
    }
    return crossings % 2 == 1
}

var minX = 0
var maxX = 0
var minY = 0
var maxY = 0

val instructions = generateSequence(::readlnOrNull)
        .map(Instruction::parse)
        .toList()

var x = 0
var y = 0
for (instruction in instructions) {
    x += instruction.direction.dx * instruction.count
    y += instruction.direction.dy * instruction.count
    minX = min(minX, x)
    maxX = max(maxX, x)
    minY = min(minY, y)
    maxY = max(maxY, y)
}

if (debug) println("minX=$minX maxX=$maxX minY=$minY maxY=$maxY")

val width = maxX - minX + 1
val height = maxY - minY + 1
val grid = List(height) { CharArray(height) { ' ' } }
x = -minX
y = -minY
grid[y][x] = '#'
for (instruction in instructions) {
    grid.handle(x, y, instruction)
    x += instruction.direction.dx * instruction.count
    y += instruction.direction.dy * instruction.count
}

grid.print()

val outside = ArrayDeque(
        grid[0].indices.asSequence().flatMap { sequenceOf(it to 0, it to grid.lastIndex) }
                .plus(grid.indices.asSequence().flatMap { sequenceOf(0 to it, grid[0].lastIndex to it) })
                .filter { (x, y) -> grid[y][x] != '#' }
                .toList()
)

val visited = outside.toMutableSet()
while (outside.isNotEmpty()) {
    val (x, y) = outside.removeLast()
    if (debug) println("Marking $x, $y as outside")
    grid[y][x] = '.'
    for (direction in Direction.entries) {
        val nx = x + direction.dx
        val ny = y + direction.dy
        if (ny !in 0..grid.lastIndex || nx !in 0..grid[ny].lastIndex) continue
        if (grid[ny][nx] == '#') continue
        val p = nx to ny
        if (p in visited) continue
        if (visited.add(p)) {
            outside.add(p)
        }
    }
}

for (row in grid) {
    for (x in row.indices) {
        if (row[x] == ' ') {
            row[x] = '#'
        }
    }
}
grid.print()

println(grid.sumOf { row -> row.count { it == '#' } })

println(start.elapsedNow())