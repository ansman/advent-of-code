#!/usr/bin/env kotlin

import java.text.ParsePosition

sealed class Packet {
    data class Single(val value: Int) : Packet() {
        override fun toString(): String = value.toString()
    }

    data class Multiple(val values: List<Packet>) : Packet() {
        constructor(vararg values: Packet) : this(values.asList())
        override fun toString(): String = values.joinToString(",", prefix = "[", postfix = "]")
    }
}

fun String.readAndConsume(position: ParsePosition = ParsePosition(0)): Packet =
        if (get(position.index) == '[') {
            ++position.index
            Packet.Multiple(buildList {
                while (this@readAndConsume.get(position.index) != ']') {
                    add(readAndConsume(position))
                }
                // Skips ]
                ++position.index
                if (this@readAndConsume.getOrNull(position.index) == ',') ++position.index
            })
        } else {
            val start = position.index
            while (position.index < length && get(position.index).isDigit()) {
                ++position.index
            }
            val end = position.index
            if (get(position.index) == ',') ++position.index
            Packet.Single(substring(start, end).toInt())
        }

fun compare(left: Packet, right: Packet, indent: String = ""): Int {
    return when (left) {
        is Packet.Single ->
            when (right) {
                is Packet.Single -> {
                    when {
                        left.value < right.value -> -1
                        left.value > right.value -> 1
                        else -> 0
                    }
                }

                is Packet.Multiple -> compare(Packet.Multiple(left), right, indent + "  ")
            }

        is Packet.Multiple ->
            when (right) {
                is Packet.Single -> compare(left, Packet.Multiple(right), indent + "  ")

                is Packet.Multiple -> {
                    var i = 0
                    while (i < left.values.size || i < right.values.size) {
                        val l = left.values.getOrNull(i) ?: return -1
                        val r = right.values.getOrNull(i) ?: return 1
                        val result = compare(l, r, indent + "  ")
                        if (result != 0) {
                            return result
                        }
                        ++i
                    }
                    0
                }
            }
    }
}

val p1 = Packet.Multiple(Packet.Multiple(Packet.Single(2)))
val p2 = Packet.Multiple(Packet.Multiple(Packet.Single(6)))
val packets = buildList {
    add(p1)
    add(p2)
    while (true) {
        val line = readLine() ?: break
        if (line.isNotBlank()) {
            add(line.readAndConsume())
        }
    }
}


val sorted = packets.sortedWith(::compare)
val i1 = sorted.indexOf(p1) + 1
val i2 = sorted.indexOf(p2) + 1
println(i1 * i2)