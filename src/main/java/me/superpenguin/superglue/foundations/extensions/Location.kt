package me.superpenguin.superglue.foundations.extensions

import org.bukkit.Location

/** Get the top-center of the block represented by this location */
fun Location.toCenter() = Location(world, blockX + 0.5, blockY + 0.5, blockZ + 0.5, yaw, pitch)