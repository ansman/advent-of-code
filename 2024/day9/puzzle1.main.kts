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

data class FileBlock(val id: Int)

val fileSystem = arrayListOf<Long?>()
input.forEachIndexed { i, c ->
    if (i % 2 == 0) {
        repeat(c.digitToInt()) { fileSystem.add(i / 2L) }
    } else {
        repeat(c.digitToInt()) { fileSystem.add(null) }
    }
}


fun printFileSystem() {
if (debug) {
    println(fileSystem.joinToString("") { it?.toString() ?: "." })
}
    }

fun findNextEmptyIndex(start: Int = 0): Int {
    var i = start
    while (fileSystem[i] != null) {
        ++i
    }
    return i
}

fun findNextByte(start: Int = fileSystem.lastIndex): Int {
    var i = start
    while (fileSystem[i] == null) {
        --i
    }
    return i
}

printFileSystem()
var empty = findNextEmptyIndex()
var lastByte = findNextByte()
while (empty < lastByte) {
    fileSystem[empty] = fileSystem[lastByte]
    fileSystem[lastByte] = null
    empty = findNextEmptyIndex(empty + 1)
    lastByte = findNextByte(lastByte - 1)
}
printFileSystem()

val checksum = fileSystem.withIndex().sumOf { (i, id) -> i * (id ?: 0) }

println("Checksum is $checksum")
println("Ran in ${start.elapsedNow()}")