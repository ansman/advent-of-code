#!/usr/bin/env kotlin


import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

@Suppress("EnumEntryName")
enum class Instruction(val getOperand: Computer.() -> Long) {
    adv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Long) {
            a = div(a, operand)
            ip += 2
        }
    },

    bxl(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Long) {
            b = b xor operand
            ip += 2
        }
    },

    bst(Computer::getComboOperand) {
        override fun Computer.execute(operand: Long) {
            b = operand % 8
            ip += 2
        }
    },

    jnz(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Long) {
            if (a == 0L) {
                ip += 2
            } else {
                ip = operand.toInt()
            }
        }
    },

    bxc(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Long) {
            b = b xor c
            ip += 2
        }
    },

    `out`(Computer::getComboOperand) {
        override fun Computer.execute(operand: Long) {
            output += (operand % 8).toInt()
            ip += 2
        }
    },

    bdv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Long) {
            b = div(a, operand)
            ip += 2
        }
    },

    cdv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Long) {
            c = div(a, operand)
            ip += 2
        }
    };

    abstract fun Computer.execute(operand: Long)
}

class Computer {
    var a = 0L
    var b = 0L
    var c = 0L
    var ip = 0
    var program = IntArray(0)
    val output = mutableListOf<Int>()
    var expectedOutput = 0

    fun div(num: Long, operand: Long): Long = num ushr operand.toInt()

    fun getLiteralOperand(): Long = program[ip + 1].toLong()

    fun getComboOperand(): Long =
        when (val v = program[ip + 1]) {
            in 0..3 -> v.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> error("Invalid operand $v")
        }

    fun load() {
        readln()
        readln()
        readln()
        readln()
        program = readln().removePrefix("Program: ").split(',').map { it.toInt() }.toIntArray()
    }

    fun run(a: Long, expectedOutput: Int): Boolean {
        this.a = a
        this.b = 0
        this.b = 0
        this.expectedOutput = expectedOutput
        ip = 0
        output.clear()
        while (ip in 0..(program.size - 2)) {
            val opCode = program[ip]
            val instruction = Instruction.entries[opCode]
            val operand = instruction.getOperand(this)
            with(instruction) {
                execute(operand)
            }
            if (output.isNotEmpty()) {
                return output.first() == expectedOutput
            }
        }
        return false
    }
}

fun Int.toBinary(): String = buildString {
    repeat(Int.SIZE_BITS) {
        val bit = (this@toBinary ushr (Int.SIZE_BITS - 1 - it) and 1)
        val append = if (bit != 0) "1" else if (isNotEmpty()) "0" else null
        if (append != null) {
            if (isEmpty()) {
                append("0b")
            }
            append(append)
        }
    }
    if (isEmpty()) {
        append("0b0")
    }
}

fun Computer.possibleValuesFor(position: Int = 0): Sequence<Long> {
    if (position >= program.size) {
        return sequenceOf(0)
    }

    return possibleValuesFor(position + 1).flatMap { a ->
        sequence {
            for (lsb in 0b000..0b111L) {
                val value = (a shl 3) or lsb
                if (run(value, program[position])) {
                    yield(value)
                }
            }
        }
    }
}

with(Computer()) {
    load()
    val a = possibleValuesFor().first()
    println("Smallest value for A is $a")
}

println("Ran in ${startTime.elapsedNow()}")

//val initialComputer = Computer().apply { load() }
//val cores = Runtime.getRuntime().availableProcessors()
//
//if (debug) {
//    val computer = initialComputer.copy()
//    computer.print = true
//    computer.run(100)
//    exitProcess(0)
//}
//
//repeat(cores) { core ->
//    Thread {
//        val computer = initialComputer.copy()
//        var i = core * 100_000_000_000_000
//        var s = TimeSource.Monotonic.markNow()
//        val printEvery = 10_000_000L
//        while (true) {
//            computer.reset(i, initialComputer.b, initialComputer.c)
//            if (debug && i % printEvery == 0L) {
//                val duration = s.elapsedNow().toDouble(DurationUnit.SECONDS)
//                val ops = "%d".format(Locale.US, ((printEvery * cores) / duration).roundToLong())
//                debug("Running with a=$i ($ops ops/s)")
//                s = TimeSource.Monotonic.markNow()
//            }
//            try {
//                if (computer.run(Int.MAX_VALUE)) {
//                    println(i)
//                    println("Ran in ${startTime.elapsedNow()}")
//                    exitProcess(0)
//                }
//            } catch (ignored: Exception) {
//            }
//            ++i
//        }
//    }.start()
//}