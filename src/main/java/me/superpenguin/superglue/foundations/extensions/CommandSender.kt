package me.superpenguin.superglue.foundations.extensions

import org.bukkit.command.CommandSender

/** Send a colored message to a player */
fun CommandSender.send(msg: String, hex: Boolean = false) = sendMessage(msg.toColor(hex))

