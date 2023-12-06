package com.github.supergluelib.foundation.misc

import org.bukkit.Location
import org.bukkit.World

data class BlockPos(val x: Int, val y: Int, val z: Int) {
    fun getLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}
