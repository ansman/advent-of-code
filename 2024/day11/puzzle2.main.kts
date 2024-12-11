#!/usr/bin/env kotlin

@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()


val iterations = 75

val jobs = mutableMapOf<Pair<Long, Int>, Deferred<Long>>()

suspend fun CoroutineScope.iterate(
    number: Long,
    iterationsRemaining: Int = iterations,
): Long {
    val job = jobs.getOrPut(number to iterationsRemaining) {
        async {
            if (iterationsRemaining == 0) {
                // We only have one number left
                return@async 1
            }
            if (number == 0L) {
                return@async iterate(1L, iterationsRemaining - 1)
            }
            val digits = log10(number.toDouble()).toLong() + 1
            if (digits % 2L != 0L) {
                return@async iterate(number * 2024, iterationsRemaining - 1)
            }
            val split = 10.0.pow(digits / 2.0).toLong()
            val left = number / split
            val right = number % split
            iterate(left, iterationsRemaining - 1) + iterate(right, iterationsRemaining - 1)
        }
    }
    return job.await()
}

var numbers = readln().split(' ').map { it.toLong() }
val numberOfStones = runBlocking {
    withContext(Dispatchers.Default) {
        numbers.sumOf {
            iterate(it)
        }
    }
}

println("Number of stones is $numberOfStones")
println("Ran in ${start.elapsedNow()}")
