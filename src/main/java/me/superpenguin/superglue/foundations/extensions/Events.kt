package me.superpenguin.superglue.foundations.extensions

import me.superpenguin.superglue.foundations.customevents.PlayerMoveBlockEvent
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.java.JavaPlugin


/** Easy Listener registering :D */
fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)
/** Checks if player click was in top inventory not bottom inventory */
fun InventoryClickEvent.clickedTopInventory() = clickedInventory?.equals(view.topInventory) == true

fun registerAllListeners( plugin: JavaPlugin, vararg listeners: Listener) = listeners.forEach { it.register(plugin) }