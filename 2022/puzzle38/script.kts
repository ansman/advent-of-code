#!/usr/bin/env kotlin

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min
import kotlin.script.dependencies.ScriptContents
import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) {
    log { msg }
}

fun log(msg: () -> Any?) {
    if (logging) {
        println(msg())
    }
}

enum class Material {
    Geode,
    Obsidian,
    Clay,
    Ore;

    companion object {
        fun parse(input: String): Material = requireNotNull(values().find { it.name.equals(input, ignoreCase = true) }) {
            "Unknown material $input"
        }
    }
}

interface MaterialMap {
    operator fun get(material: Material): Int
    fun toMutableMap(): MutableMaterialMap
}

inline fun Map<Material, Int>.toMaterialMap() = ArrayMaterialMap(this)

inline fun MaterialMap.forEach(block: (Material, Int) -> Unit) {
    for (material in Material.values()) {
        block(material, get(material))
    }
}

interface MutableMaterialMap : MaterialMap {
    operator fun set(material: Material, quantity: Int)
}

operator fun MutableMaterialMap.plusAssign(other: MaterialMap) = other.forEach { material, q -> add(material, q) }
operator fun MutableMaterialMap.minusAssign(other: MaterialMap) = other.forEach { material, q -> add(material, -q) }
fun MutableMaterialMap.add(material: Material, amount: Int) {
    this[material] = get(material) + amount
}

fun MutableMaterialMap.increment(material: Material) = add(material, 1)
fun MutableMaterialMap.decrement(material: Material) = add(material, -1)

fun buildMaterialMap(builder: MutableMaterialMap.() -> Unit): MaterialMap = ArrayMaterialMap().apply(builder)

class ArrayMaterialMap private constructor(private val array: IntArray) : MutableMaterialMap {
    constructor() : this(IntArray(Material.values().size))
    constructor(map: Map<Material, Int>) : this() {
        for ((m, q) in map) {
            set(m, q)
        }
    }

    override fun get(material: Material): Int = array[material.ordinal]

    override fun set(material: Material, quantity: Int) {
        require(quantity >= 0)
        array[material.ordinal] = quantity
    }

    operator fun plusAssign(other: MaterialMap) {
        array.forEachIndexed { i, v ->
            array[i] = v + other.get(Material.values()[i])
        }
    }

    override fun equals(other: Any?): Boolean =
            other === this || other is ArrayMaterialMap && array.contentEquals(other.array)

    override fun hashCode(): Int = array.contentHashCode()

    override fun toString(): String = buildString {
        append('{')
        array.forEachIndexed { i, v ->
            if (v == 0) return@forEachIndexed
            if (length > 1) {
                append(", ")
            }
            append(Material.values()[i]).append('=').append(v)
        }
        append('}')
    }

    override fun toMutableMap(): MutableMaterialMap = ArrayMaterialMap(array.copyOf())
}

data class Robot(
        val collects: Material,
        val costs: MaterialMap
) {
    companion object {
        private val pattern = Regex("""Each (\w+) robot costs (.+)""")
        fun parse(input: String): Robot {
            val (material, costs) = pattern.matchEntire(input)!!.destructured
            return Robot(
                    collects = Material.parse(material),
                    costs = costs
                            .split(" and ")
                            .map { it.split(" ") }
                            .associateBy({ (_, m) -> Material.parse(m) }, { (c, _) -> c.toInt() })
                            .let(::ArrayMaterialMap)
            )
        }
    }
}

data class Blueprint(
        val id: Int,
        val robots: List<Robot>,
) {
    val geodeRobot = robots.single { it.collects == Material.Geode }

    val maxCostPerMaterial = buildMaterialMap {
        for (robot in robots) {
            robot.costs.forEach { material, q ->
                set(material, max(q, get(material)))
            }
        }
    }
}

private val blueprintPattern = Regex("""Blueprint (\d+): (.+)""")
fun parseBlueprint(input: String): Blueprint {
    val (id, robots) = blueprintPattern.matchEntire(input)!!.destructured
    return Blueprint(
            id = id.toInt(),
            robots = robots
                    .removeSuffix(".")
                    .split(". ")
                    .map { Robot.parse(it) }
                    .sortedBy { it.collects }
    )
}

data class SimulationStep(
        val minutesRemaining: Int,
        val collectionRate: MaterialMap,
        val inventory: MaterialMap,
) {
    init {
        require(minutesRemaining >= 0)
    }

    val maxNumberOfGeodes: Int
        get() {
            var max = inventory[Material.Geode]
            repeat(minutesRemaining) {
                max += it
            }
            return max
        }

    fun nextMinute(purchasedRobots: List<Robot>): SimulationStep {
        val newInventory = inventory.toMutableMap()
        val newCollectionRate = collectionRate.toMutableMap()
        for (robot in purchasedRobots) {
            newInventory -= robot.costs
            if (robot.collects == Material.Geode) {
                // Credit geodes right away
                newInventory.add(Material.Geode, minutesRemaining - 1)
            } else {
                newCollectionRate.increment(robot.collects)
            }
        }
        newInventory += collectionRate

        return SimulationStep(
                minutesRemaining = minutesRemaining - 1,
                collectionRate = newCollectionRate,
                inventory = newInventory
        )
    }

//    private val mutableInventory = mutableMapOf<Material, Int>()
//    fun canAfford(robots: Map<Robot, Int>): Boolean {
//        mutableInventory.clear()
//        mutableInventory.putAll(inventory)
//        for ((robot, quantity) in robots) {
//            for ((material, cost) in robot.costs) {
//                val amount = mutableInventory.getOrDefault(material, 0) - cost * quantity
//                if (amount >= 0) {
//                    mutableInventory[material] = amount
//                } else {
//                    return false
//                }
//            }
//        }
//        return true
//    }

    fun timeUntilCanAfford(robot: Robot): Int {
        var timeUntilCanAfford = 0
        robot.costs.forEach { m, c ->
            // We already have enough
            if (inventory[m] >= c) {
                return@forEach
            }
            val rate = collectionRate[m]
            if (rate == 0) return Int.MAX_VALUE
            timeUntilCanAfford = max(timeUntilCanAfford, (c - inventory[m] + rate - 1) / rate)
            if (timeUntilCanAfford > minutesRemaining) {
                return Int.MAX_VALUE
            }
        }
        return timeUntilCanAfford
    }

    fun fastForward(byMinutes: Int): SimulationStep {
        require(byMinutes > 0 && byMinutes < Int.MAX_VALUE)
        return copy(
                minutesRemaining = minutesRemaining - byMinutes,
                inventory = inventory.toMutableMap().apply {
                    collectionRate.forEach { m, r ->
                        set(m, get(m) + r * byMinutes)
                    }
                }
        )
    }

    fun stepWhenCanAfford(robot: Robot): SimulationStep? {
        val timeUntilCanAfford = timeUntilCanAfford(robot)
        require(timeUntilCanAfford > 0)
        if (timeUntilCanAfford == Int.MAX_VALUE) return null
        return fastForward(timeUntilCanAfford)
    }

    fun canAfford(robot: Robot): Boolean {
        robot.costs.forEach { material, cost ->
            if (inventory[material] < cost) return false
        }
        return true
    }

//
//    fun getMaxPurchaseCount(robot: Robot): Int {
//        var budget = Int.MAX_VALUE
//        for ((m, c) in robot.costs) {
//            budget = min(
//                    budget,
//                    inventory.getOrDefault(m, 0) / c
//            )
//            if (budget == 0) {
//                return 0
//            }
//        }
//        return budget
//    }
}


fun Blueprint.runSimulation(minutes: Int): Int {
    val memo = mutableMapOf<SimulationStep, Int>()

    var iterations = 0
    var bestSoFar = 0
    fun SimulationStep.needsAdditionalRobot(robot: Robot): Boolean {
        if (robot.collects == Material.Geode) return true
        val maxCost = maxCostPerMaterial[robot.collects]
        val rate = collectionRate[robot.collects]
        if (rate >= maxCost) {
            // No need for an additional robot because we'll get all we need every step
            return false
        }
        // TODO: Check this
        if (rate * minutesRemaining + inventory[robot.collects] >= minutesRemaining * maxCost) {
            return false
        }
        return true
    }

    fun SimulationStep.run(): Int {
        if (minutesRemaining <= 1) return inventory[Material.Geode]
        if (maxNumberOfGeodes <= bestSoFar) return 0
        return memo.getOrPut(this) {
//            if (++iterations % 10_000_000 == 0) {
//                println("Iteration $iterations")
//            }
            log("")
            log { "Running $this" }
            // Since we credit geodes immediately, we can just the current inventory to see what would happen if we did nothing
            var max = inventory[Material.Geode]
            for (robot in robots) {
                log { "Trying $robot" }
                if (!needsAdditionalRobot(robot)) {
                    log { "Doesn't need additional robot" }
                    continue
                }

                // If we can't afford it now, wait until we can
                if (!canAfford(robot)) {
                    log { "Can't afford robot, waiting until we can" }
                    val next = stepWhenCanAfford(robot) ?: continue
                    log { "Can afford robot at minute ${next.minutesRemaining}" }
                    max = max(max, next.run())
                } else {
                    // If we can afford, we either try to buy it now, or fast forward until we can buy another robot
                    // TODO: Try buying multiple robots
                    log { "Can afford, trying to buy one" }
                    max = max(max, nextMinute(listOf(robot)).run())
                    var timeUntilNextRobot = Int.MAX_VALUE
                    for (r in robots) {
                        if (r != robot && needsAdditionalRobot(r)) {
                            timeUntilNextRobot = min(timeUntilNextRobot, timeUntilCanAfford(r))
                        }
                    }
                    if (timeUntilNextRobot > 0 && timeUntilNextRobot < Int.MAX_VALUE) {
                        log { "Also waiting $timeUntilNextRobot and trying again" }
                        max = max(max, fastForward(timeUntilNextRobot).run())
                    }
                }
            }
            bestSoFar = max(bestSoFar, max)
            return max
        }
    }
//
//    fun SimulationStep.run(memo: MutableMap<SimulationStep, Int>): Int {
//        if (minutesRemaining <= 0) return inventory.getOrDefault(Material.Geode, 0)
//        return run { //memo.getOrPut(this) {
//            var max = 0
//            for (robot in robots) {
//                if (!needsAdditionalRobot(robot)) continue
//                if (canAfford(robot)) {
//                    max = max(max, nextMinute(robot).run(memo))
//                    if (robot.collects == Material.Geode) {
//                        // No need to try any other robots
//                        break
//                    }
//                } else {
//                    val next = stepWhenCanAfford(robot) ?: continue
//                    max = max(max, next.run(memo))
//                }
//            }
//            max
//        }
//    }
//    return SimulationStep(
//            minutesRemaining = 24,
//            collectionRate = mapOf(Material.Ore to 1),
//            inventory = mapOf()
//    ).run(mutableMapOf())


//    var iterationCount = 0
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    fun runRecursive(step: SimulationStep, memo: MutableMap<SimulationStep, Int>): Int {
//        if (step.minutesRemaining <= 0) return step.inventory.getOrDefault(Material.Geode, 0)
////        return memo.getOrPut(step) {
//        ++iterationCount
////        if (iterationCount % 10_000 == 0) {
////            println("Blueprint $id has run $iterationCount iterations")
////        }
//
//        log(step)
//        val geodeBuyCount = step.getMaxPurchaseCount(geodeRobot)
//        if (geodeBuyCount > 0) {
//            return runRecursive(step.stepped(mapOf(geodeRobot to geodeBuyCount)), memo)
//        }
//
//        val purchasedRobots = mutableMapOf<Robot, Int>()
//
//        fun tryBuyingRobots(): Int {
//            var max = 0
//            for (robot in robots) {
//                if (!step.needsAdditionalRobot(robot)) {
//                    // No need to even check this robot
//                    continue
//                }
//                // Try buying the robot
//                purchasedRobots.increment(robot)
//                try {
//                    if (step.canAfford(purchasedRobots)) {
//                        // If we can afford this, try buying it
//                        max = max(max, tryBuyingRobots())
//                        continue
//                    }
//                } finally {
//                    purchasedRobots.decrement(robot)
//                }
//                // If we couldn't buy this robot, try fast forwarding until we can
//                if (purchasedRobots.isEmpty()) {
//                    val next = step.stepWhenCanAfford(robot)
//                    if (next != null) {
//                        max = max(max, runRecursive(next, memo))
//                    }
//                }
//            }
//            //
//            if (purchasedRobots.isNotEmpty()) {
//                max = max(max, runRecursive(step.stepped(purchasedRobots), memo))
//            }
//
//            return max
//        }
//        return tryBuyingRobots()
//    }
//

//    if (id == 1) {
//        SimulationStep(
//                minutesRemaining = minutes,
//                collectionRate = mapOf(Material.Ore to 1).toMaterialMap(),
//                inventory = ArrayMaterialMap()
//        ).timeUntilCanAfford(Material.Cl)
//    }

    return SimulationStep(
            minutesRemaining = minutes,
            collectionRate = mapOf(Material.Ore to 1).toMaterialMap(),
            inventory = ArrayMaterialMap()
    ).run()
    return 0
}

val total = AtomicInteger(1)
val threads = mutableListOf<Thread>()

var i = 0
while (i < 3) {
    val blueprint = parseBlueprint(readLine() ?: break)

    Thread {
        println("Running simulation for $blueprint")
        val geodes = blueprint.runSimulation(32)
        println("Blueprint %d can get %d geodes".format(blueprint.id, geodes))
        println()
        total.updateAndGet { geodes * it }
    }
            .also(threads::add)
            .start()
    ++i
}
threads.forEach(Thread::join)
println("Total: ${total.get()}")