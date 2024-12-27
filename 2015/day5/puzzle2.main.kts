#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = true
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

fun String.isNice(): Boolean {
    var hasRepeat = false
    var hasPair = false
    forEachIndexed { i, c ->
        if (!hasPair && i < lastIndex - 1) {
            hasPair = substring(i + 2).contains(substring(i, i + 2))
        }

        if (c == getOrNull(i - 2)) {
            hasRepeat = true
        }
    }
    return when {
        !hasRepeat -> {
            debug("$this is naughty because it doesn't have a repeating character")
            false
        }

        !hasPair -> {
            debug("$this is naughty because it does not contain a double pair")
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