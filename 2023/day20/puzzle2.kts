#!/usr/bin/env kotlin

import java.util.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

sealed class Module {
    abstract val name: String
    abstract val connections: List<String>

    abstract fun reset()
    abstract fun prime(input: Module)
    abstract fun receiveFrom(module: Module, pulse: Pulse): Pulse?

    fun toModuleName(): String = buildString(name.length + 1) {
        when (this@Module) {
            is Button -> {}
            is Broadcaster -> {}
            is FlipFlop -> append('%')
            is Conjunction -> append('&')
            else -> {}
        }
        append(name)
    }

    companion object {
        fun parse(line: String): Module {
            val typeAndName = line.substringBefore(" -> ")
            val connections = line.substringAfter(" -> ").split(", ")
            return when {
                typeAndName.startsWith('%') -> FlipFlop(typeAndName.drop(1), connections)
                typeAndName.startsWith('&') -> Conjunction(typeAndName.drop(1), connections)
                typeAndName.startsWith("broadcaster") -> Broadcaster(connections)
                else -> error("Unknown type: $line")
            }
        }
    }

    data object Button : Module() {
        override val name: String get() = "button"
        override val connections: List<String> get() = listOf("broadcaster")

        override fun reset() {}
        override fun prime(input: Module) {}
        override fun receiveFrom(module: Module, pulse: Pulse): Pulse =
                error("Button should not receive pulses")
    }

    data class Broadcaster(override val connections: List<String>) : Module() {
        override val name: String get() = "broadcaster"

        override fun reset() {}
        override fun prime(input: Module) {}
        override fun receiveFrom(module: Module, pulse: Pulse): Pulse = pulse
    }

    data class FlipFlop(
            override val name: String,
            override val connections: List<String>,
            var on: Boolean = false
    ) : Module() {
        override fun reset() {
            on = false
        }

        override fun prime(input: Module) {}
        override fun receiveFrom(module: Module, pulse: Pulse): Pulse? =
                when (pulse) {
                    Pulse.Low -> when (on) {
                        false -> {
                            on = true
                            Pulse.High
                        }

                        true -> {
                            on = false
                            Pulse.Low
                        }
                    }

                    Pulse.High -> null
                }
    }

    data class Conjunction(
            override val name: String,
            override val connections: List<String>,
            private val lastPulses: MutableMap<String, Pulse> = mutableMapOf()
    ) : Module() {

        override fun reset() {
            for (entry in lastPulses.entries) {
                entry.setValue(Pulse.Low)
            }
        }

        override fun prime(input: Module) {
            lastPulses[input.name] = Pulse.Low
        }

        override fun receiveFrom(module: Module, pulse: Pulse): Pulse {
            lastPulses[module.name] = pulse
            return if (lastPulses.values.all { it == Pulse.High }) Pulse.Low else Pulse.High
        }
    }
}

enum class Pulse {
    Low,
    High
}

val modules = generateSequence(::readlnOrNull)
        .map(Module::parse)
        .associateBy { it.name }

for (module in modules.values) {
    for (connection in module.connections) {
        modules[connection]?.prime(module)
    }
}

val inputs = modules.values
        .asSequence()
        .flatMap { m -> m.connections.asSequence().map { it to m } }
        .groupBy({ it.first }, { it.second })

fun printGraph() {
    fun Any.escape(): String = toString().replace("&", "&amp;").replace("%", "\\%")
    for (module in modules.values) {
        for (connection in module.connections) {
            println("  \"${module.toModuleName().escape()}\" -> \"${modules[connection]?.toModuleName()?.escape() ?: connection}\";")
        }
    }
}

//printGraph()
//exitProcess(0)

fun Module.iterationsFor(targetPulse: Pulse): Long {
    for (module in modules.values) {
        module.reset()
    }
    var presses = 1L
    while (true) {
        if (presses % 100_000L == 0L) println("Press $presses")
        val queue = LinkedList<Pair<Module, Pulse>>(listOf(Module.Button to Pulse.Low))
        while (queue.isNotEmpty()) {
            val (originator, pulse) = queue.removeFirst()
            if (originator === this && pulse == targetPulse) {
                return presses
            }

            for (connection in originator.connections) {
                val connectionModule = modules[connection] ?: continue
                val transmit = connectionModule.receiveFrom(originator, pulse) ?: continue
                queue.add(connectionModule to transmit)
            }
        }
        ++presses
    }
}

fun List<Long>.lcm(): Long = reduce(::lcm)
fun lcm(a: Long, b: Long): Long = (a * b) / gcf(a, b)
fun gcf(a: Long, b: Long): Long = if (b == 0L) a else gcf(b, a % b)

inputs.getValue("rx")
        // Assumption: RX has a single input
        .single()
        .also { require(it is Module.Conjunction) }
        .let { inputs.getValue(it.name) }
        .map { it.iterationsFor(Pulse.High) }
        .lcm()
        .let(::println)

println(start.elapsedNow())