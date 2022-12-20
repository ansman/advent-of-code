#!/usr/bin/env kotlin


import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) {
    if (logging) {
        println(msg)
    }
}

val numbers = buildList {
    while (true) {
        add((readLine()?.toLong() ?: break) * 811589153L)
    }
}.withIndex().toList().toTypedArray()
val positionForNumber = mutableMapOf<IndexedValue<Long>, Int>()
numbers.forEachIndexed { index, v -> positionForNumber[v] = index }

fun Array<IndexedValue<Long>>.moveNumberUp(fromIndex: Int, toIndex: Int) {
    require(fromIndex < toIndex)
    val target = this[fromIndex]
    for (i in fromIndex until toIndex) {
        val v = this[i + 1]
        positionForNumber[v] = i
        this[i] = v
    }
    this[toIndex] = target
    positionForNumber[target] = toIndex
}

fun Array<IndexedValue<Long>>.moveNumberDown(fromIndex: Int, toIndex: Int) {
    require(fromIndex > toIndex)
    val target = this[fromIndex]
    for (i in fromIndex downTo  toIndex + 1) {
        val v = this[i - 1]
        positionForNumber[v] = i
        this[i] = this[i - 1]
    }
    this[toIndex] = target
    positionForNumber[target] = toIndex
}

fun printList() {
    if (logging) {
        log(numbers.joinToString(", ") { it.value.toString() })
        log(positionForNumber.entries.joinToString { "${it.key.value}=${it.value}" })
        log("")
    }
}

fun mix() {
    for ((v, i) in positionForNumber) {
        val number = numbers[i]
        if (number.value == 0L) continue

        var newIndex = i + number.value
        if (newIndex <= 0) {
            while (newIndex <= 0) {
                newIndex = newIndex % numbers.size + numbers.size - 1 + newIndex / numbers.size
            }
        } else if (newIndex >= numbers.size) {
            while (newIndex >= numbers.size) {
                newIndex = newIndex % numbers.size + newIndex / numbers.size
            }
        }

        if (newIndex < i) {
            // We're moving backwards
            numbers.moveNumberDown(i, newIndex.toInt())
        } else if (newIndex > i) {
            numbers.moveNumberUp(i, newIndex.toInt())
        }
    }
    printList()
}

printList()
repeat(10) {
    mix()
}

val zero = numbers.indexOfFirst { it.value == 0L }
println(sequenceOf(1000, 2000, 3000)
        .map { numbers[(zero + it) % numbers.size].value }
        .sum())