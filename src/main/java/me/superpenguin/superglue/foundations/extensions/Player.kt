package me.superpenguin.superglue.foundations.extensions

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

fun Player.kill() { this.health = 0.0 }
fun Player.heal() { this.health = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0 }