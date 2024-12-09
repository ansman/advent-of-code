#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()

val input = readln()

data class FileBlock(val id: Long?, val size: Int)

val fileSystem = arrayListOf<FileBlock>()
input.forEachIndexed { i, c ->
    val l = c.digitToInt()
    val block = FileBlock(if (i % 2 == 0) i / 2L else null, l)
    repeat(l) { fileSystem.add(block) }
}

fun printFileSystem() {
    if (debug) {
        println(fileSystem.joinToString("") { it.id?.toString() ?: "." })
    }
}

fun findNextEmptyIndex(start: Int = 0): Int {
    var i = start
    while (fileSystem[i].id != null) {
        ++i
    }
    return i
}

fun findStartOfBlock(start: Int = fileSystem.lastIndex): Int {
    var i = start
    while (fileSystem[i].id == null) {
        --i
    }
    return i - fileSystem[i].size + 1
}

fun findNextEmptyBlock(start: Int, size: Int, max: Int): Int {
    var i = start
    while (i < max) {
        val n = fileSystem[i]
        if (n.id == null && n.size >= size) {
            return i
        } else {
            i += n.size
        }
    }
    return -1
}

fun moveBlock(fromIndex: Int, toIndex: Int): Int {
    val from = fileSystem[fromIndex]
    val to = fileSystem[toIndex]

    repeat(from.size) { fileSystem[toIndex + it] = from }
    repeat(from.size) { fileSystem[fromIndex + it] = FileBlock(null, from.size) }
    repeat(to.size - from.size) {
        fileSystem[toIndex + from.size + it] = to.copy(size = to.size - from.size)
    }
    return toIndex + from.size
}

printFileSystem()
var firstEmptyIndex = findNextEmptyIndex()
var lastByteIndex = findStartOfBlock()
while (firstEmptyIndex < lastByteIndex) {
    debug("")
    val lastByte = fileSystem[lastByteIndex]
    debug("firstEmptyIndex=$firstEmptyIndex, lastByteIndex=$lastByteIndex, lastByte=$lastByte")
    val nextEmpty = findNextEmptyBlock(firstEmptyIndex, lastByte.size, lastByteIndex)
    if (nextEmpty == -1) {
        debug("No empty block found for block of size ${lastByte.size}")
        lastByteIndex = findStartOfBlock(lastByteIndex - 1)
        continue
    }
    debug("Moving block of ID ${lastByte.id} from $lastByteIndex to $nextEmpty")
    val e = moveBlock(lastByteIndex, nextEmpty)
    if (nextEmpty == firstEmptyIndex) {
        firstEmptyIndex = e
    }
    firstEmptyIndex = findNextEmptyIndex(firstEmptyIndex)
    lastByteIndex = findStartOfBlock(lastByteIndex - 1)
    printFileSystem()
}
printFileSystem()

val checksum = fileSystem.withIndex().sumOf { (i, block) -> i * (block.id ?: 0) }

println("Checksum is $checksum")
println("Ran in ${start.elapsedNow()}")