package me.superpenguin.superglue.foundations.extensions

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.kill() { this.health = 0.0 }
fun Player.heal() { this.health = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0 }

/** Adds the item to a players inventory and drops any items that didn't fit onto the floor */
fun Player.giveOrDropItem(item: ItemStack) = inventory.addItem(item).forEach { (_, item) ->  world.dropItem(location, item)}