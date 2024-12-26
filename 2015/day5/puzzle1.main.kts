#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

fun String.isNice(): Boolean {
    var prev = '\u0000'
    var vowelCount = 0
    var hasDoubleLetter = false
    forEach {  c ->
        when (c) {
            'a', 'e', 'i', 'o', 'u' -> ++vowelCount
        }
        if (c == prev) {
            hasDoubleLetter = true
        }
        if (prev == 'a' && c == 'b' || prev == 'c' && c == 'd' || prev == 'p' && c == 'q' || prev == 'x' && c == 'y') {
            debug("$this is naughty because of it contains $prev$c")
            return false
        }
        prev = c
    }
    return when {
        !hasDoubleLetter -> {
            debug("$this is naughty because of it doesn't contain double letter")
            false
        }
        vowelCount < 3 -> {
            debug("$this is naughty because of it contains only $vowelCount vowels")
            false
        }
        else -> {
            debug("$this is nice")
            true
        }
    }
}

generateSequence { readlnOrNull() }
    .count { it.isNice() }
    .let { println(it) }

println("Ran in ${startTime.elapsedNow()}")