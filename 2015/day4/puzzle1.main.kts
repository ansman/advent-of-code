#!/usr/bin/env kotlin

import java.security.MessageDigest
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

data class XY(val x: Int, val y: Int)

val digest = MessageDigest.getInstance("MD5")!!



generateSequence { readlnOrNull() }.forEach { input ->
    repeat(Int.MAX_VALUE) {
        digest.reset()
        digest.update(input.encodeToByteArray())
        digest.update(it.toString().encodeToByteArray())
        @OptIn(ExperimentalStdlibApi::class)
        if (digest.digest().toHexString().startsWith("00000")) {
            println(it)
            return@forEach
        }
    }
}

println("Ran in ${startTime.elapsedNow()}")