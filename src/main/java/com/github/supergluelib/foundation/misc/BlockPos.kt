package com.github.supergluelib.foundation.misc

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockState

val Location.blockPos get() = BlockPos(blockX, blockY, blockZ)
val Block.blockPos get() = location.blockPos
val BlockState.blockPos get() = location.blockPos

data class BlockPos(val x: Int, val y: Int, val z: Int) {
    fun getLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}
