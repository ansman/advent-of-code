#!/usr/bin/env kotlin

sealed class DiskObject {
    abstract val name: String

    abstract val totalSize: Long

    data class File(override val name: String, val size: Long) : DiskObject() {
        override val totalSize: Long get() = size
    }

    data class Directory(
            override val name: String,
            var objects: MutableList<DiskObject> = mutableListOf(),
    ) : DiskObject() {
        override val totalSize: Long by lazy { objects.sumOf { it.totalSize } }
    }
}

operator fun DiskObject.Directory.get(name: String): DiskObject? = objects.find { it.name == name }
val DiskObject.allObjects: Sequence<DiskObject>
    get() = sequenceOf(this)
            .plus(when (this) {
                is DiskObject.Directory -> objects.asSequence().flatMap { it.allObjects }
                is DiskObject.File -> emptySequence()
            })

var line: String? = readLine()
val commandPrefix = "$ "

fun readObjects(): MutableList<DiskObject> {
    val objects = mutableListOf<DiskObject>()
    line = readLine()
    while (line != null && !line!!.startsWith(commandPrefix)) {
        val (type, name) = line!!.split(" ")
        objects.add(if (type == "dir") {
            DiskObject.Directory(name)
        } else {
            DiskObject.File(name, type.toLong())
        })
        line = readLine()
    }
    return objects
}

val currentPath = mutableListOf<DiskObject.Directory>(DiskObject.Directory("/"))

while (line != null) {
    val command = line!!.removePrefix(commandPrefix).split(" ")
    when (command[0]) {
        "cd" -> {
            val path = command[1]
            if (path == "..") {
                if (currentPath.size > 1) {
                    currentPath.removeLast()
                }
            } else if (path == "/") {
                currentPath.subList(1, currentPath.size).clear()
            } else {
                var next = currentPath.last()[path] as DiskObject.Directory?
                        ?: DiskObject.Directory(path)
                                .also { currentPath.last().objects.add(it) }
                currentPath.add(next)
            }
            line = readLine()
        }

        "ls" -> {
            currentPath.last().objects = readObjects()
        }

        else -> error("Unknown command $command")
    }
}

fun DiskObject.print(indent: String = "") {
    when (this) {
        is DiskObject.Directory -> {
            println("$indent- $name (dir, totalSize=$totalSize)")
            for (obj in objects) {
                obj.print(indent + "  ")
            }
        }
        is DiskObject.File -> {
            println("$indent- $name (file, size=$size)")
        }
    }
}

currentPath.first().print()
println(
        currentPath.first().allObjects
        .filterIsInstance<DiskObject.Directory>()
        .map { it.totalSize }
        .filter { it <= 100000L }
        .sum()
)
