#!/usr/bin/env kotlin

data class Board(val width: Int, val height: Int, val counts: IntArray)

data class Orientation(val width: Int, val height: Int, val cells: IntArray)

data class ShapeInfo(val area: Int, val orientations: List<Orientation>)

data class Placement(val indices: IntArray, val masks: LongArray)

data class CellsWithSize(val cells: IntArray, val width: Int, val height: Int)

fun normalize(cells: IntArray): CellsWithSize {
    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    for (cell in cells) {
        val x = cell ushr 16
        val y = cell and 0xFFFF
        if (x < minX) minX = x
        if (y < minY) minY = y
        if (x > maxX) maxX = x
        if (y > maxY) maxY = y
    }
    val width = maxX - minX + 1
    val height = maxY - minY + 1
    val normalized = IntArray(cells.size)
    for (i in cells.indices) {
        val x = (cells[i] ushr 16) - minX
        val y = (cells[i] and 0xFFFF) - minY
        normalized[i] = (x shl 16) or y
    }
    return CellsWithSize(normalized, width, height)
}

fun rotate90(cells: IntArray, width: Int, height: Int): CellsWithSize {
    val rotated = IntArray(cells.size)
    for (i in cells.indices) {
        val x = cells[i] ushr 16
        val y = cells[i] and 0xFFFF
        val nx = height - 1 - y
        val ny = x
        rotated[i] = (nx shl 16) or ny
    }
    return CellsWithSize(rotated, height, width)
}

fun flipHorizontal(cells: IntArray, width: Int, height: Int): CellsWithSize {
    val flipped = IntArray(cells.size)
    for (i in cells.indices) {
        val x = cells[i] ushr 16
        val y = cells[i] and 0xFFFF
        val nx = width - 1 - x
        flipped[i] = (nx shl 16) or y
    }
    return CellsWithSize(flipped, width, height)
}

fun cellsKey(cells: IntArray): String {
    val sorted = cells.copyOf()
    sorted.sort()
    return sorted.joinToString(",")
}

fun generateOrientations(baseCells: IntArray, baseWidth: Int, baseHeight: Int): List<Orientation> {
    val seen = HashSet<String>()
    val result = ArrayList<Orientation>()
    var cells = baseCells
    var width = baseWidth
    var height = baseHeight
    repeat(4) {
        fun addOrientation(cellsIn: IntArray) {
            val normalized = normalize(cellsIn)
            val key = cellsKey(normalized.cells)
            if (seen.add(key)) {
                result.add(Orientation(normalized.width, normalized.height, normalized.cells))
            }
        }
        addOrientation(cells)
        val flipped = flipHorizontal(cells, width, height)
        addOrientation(flipped.cells)
        val rotated = rotate90(cells, width, height)
        cells = rotated.cells
        width = rotated.width
        height = rotated.height
    }
    return result
}

fun buildShapeInfo(shapeLines: List<String>): ShapeInfo {
    val cellCount = shapeLines.sumOf { line -> line.count { it == '#' } }
    val cells = IntArray(cellCount)
    var idx = 0
    for (y in shapeLines.indices) {
        val line = shapeLines[y]
        for (x in line.indices) {
            if (line[x] == '#') {
                cells[idx++] = (x shl 16) or y
            }
        }
    }
    val normalized = normalize(cells)
    val orientations = generateOrientations(normalized.cells, normalized.width, normalized.height)
    return ShapeInfo(area = normalized.cells.size, orientations = orientations)
}

fun buildPlacements(
    orientations: List<Orientation>,
    boardWidth: Int,
    boardHeight: Int,
    bitsetLen: Int,
): List<Placement> {
    val placements = ArrayList<Placement>()
    val temp = LongArray(bitsetLen)
    val touched = IntArray(bitsetLen)
    for (orientation in orientations) {
        if (orientation.width > boardWidth || orientation.height > boardHeight) continue
        val cells = orientation.cells
        val maxY = boardHeight - orientation.height
        val maxX = boardWidth - orientation.width
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                var touchedCount = 0
                for (cell in cells) {
                    val cx = cell ushr 16
                    val cy = cell and 0xFFFF
                    val idx = (y + cy) * boardWidth + (x + cx)
                    val word = idx ushr 6
                    val bit = idx and 63
                    if (temp[word] == 0L) {
                        touched[touchedCount++] = word
                    }
                    temp[word] = temp[word] or (1L shl bit)
                }
                val indices = IntArray(touchedCount)
                val masks = LongArray(touchedCount)
                for (i in 0 until touchedCount) {
                    val word = touched[i]
                    indices[i] = word
                    masks[i] = temp[word]
                    temp[word] = 0L
                }
                placements.add(Placement(indices, masks))
            }
        }
    }
    return placements
}

fun canFit(board: Board, shapes: List<ShapeInfo>): Boolean {
    val boardArea = board.width * board.height
    var totalArea = 0
    for (i in shapes.indices) {
        totalArea += board.counts[i] * shapes[i].area
    }
    if (totalArea > boardArea) return false
    if (totalArea == 0) return true

    val bitsetLen = (boardArea + 63) / 64
    val placementsByType = Array(shapes.size) { emptyList<Placement>() }
    for (i in shapes.indices) {
        val count = board.counts[i]
        if (count == 0) continue
        val placements = buildPlacements(shapes[i].orientations, board.width, board.height, bitsetLen)
        if (placements.size < count) return false
        placementsByType[i] = placements
    }

    val occupied = LongArray(bitsetLen)
    val remainingCounts = board.counts.copyOf()
    val shapeAreas = IntArray(shapes.size) { shapes[it].area }

    fun overlaps(p: Placement): Boolean {
        val idxs = p.indices
        val masks = p.masks
        for (i in idxs.indices) {
            val idx = idxs[i]
            if ((occupied[idx] and masks[i]) != 0L) return true
        }
        return false
    }

    fun apply(p: Placement) {
        val idxs = p.indices
        val masks = p.masks
        for (i in idxs.indices) {
            val idx = idxs[i]
            occupied[idx] = occupied[idx] or masks[i]
        }
    }

    fun remove(p: Placement) {
        val idxs = p.indices
        val masks = p.masks
        for (i in idxs.indices) {
            val idx = idxs[i]
            occupied[idx] = occupied[idx] xor masks[i]
        }
    }

    fun dfs(remainingArea: Int): Boolean {
        if (remainingArea == 0) return true
        var bestType = -1
        var bestFit = Int.MAX_VALUE
        for (i in shapes.indices) {
            val count = remainingCounts[i]
            if (count == 0) continue
            val placements = placementsByType[i]
            var fit = 0
            for (p in placements) {
                if (!overlaps(p)) {
                    fit++
                    if (fit >= count && fit >= bestFit) break
                }
            }
            if (fit < count) return false
            if (fit < bestFit) {
                bestFit = fit
                bestType = i
            }
        }
        val placements = placementsByType[bestType]
        val area = shapeAreas[bestType]
        remainingCounts[bestType]--
        for (p in placements) {
            if (!overlaps(p)) {
                apply(p)
                if (dfs(remainingArea - area)) return true
                remove(p)
            }
        }
        remainingCounts[bestType]++
        return false
    }

    return dfs(totalArea)
}

val lines = generateSequence { readlnOrNull() }.toList()

val shapeHeader = Regex("""^\d+:\s*$""")
val boardHeader = Regex("""^(\d+)x(\d+):\s*(.*)$""")

val shapeLinesList = mutableListOf<List<String>>()
var index = 0
while (index < lines.size) {
    val line = lines[index].trim()
    if (line.isEmpty()) {
        index++
        continue
    }
    if (boardHeader.matches(line)) break
    if (shapeHeader.matches(line)) {
        index++
        val shapeLines = mutableListOf<String>()
        while (index < lines.size && lines[index].isNotBlank()) {
            shapeLines.add(lines[index].trimEnd())
            index++
        }
        shapeLinesList.add(shapeLines)
    } else {
        index++
    }
}

val shapes = shapeLinesList.map { buildShapeInfo(it) }

val boards = mutableListOf<Board>()
for (i in index until lines.size) {
    val line = lines[i].trim()
    if (line.isEmpty()) continue
    val match = boardHeader.matchEntire(line) ?: continue
    val width = match.groupValues[1].toInt()
    val height = match.groupValues[2].toInt()
    val counts = match.groupValues[3].trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        .map { it.toInt() }.toIntArray()
    boards.add(Board(width, height, counts))
}

var solvable = 0
for (board in boards) {
    if (canFit(board, shapes)) solvable++
}

println(solvable)
