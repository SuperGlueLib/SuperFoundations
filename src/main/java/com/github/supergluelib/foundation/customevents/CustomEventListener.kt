package com.github.supergluelib.foundation.customevents

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Run CustomEventListener#setup(JavaPlugin) to setup the listener and enable custom events
 */
object CustomEventListener: Listener {

    private lateinit var plugin: JavaPlugin

    fun setup(plugin: JavaPlugin) {
        CustomEventListener.plugin = plugin
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val to = event.to ?: return
        val from = event.from

        if (from.blockX != to.blockX || from.blockZ != to.blockZ || from.blockY != to.blockY) {
            Bukkit.getPluginManager().callEvent(PlayerMoveBlockEvent(event.player, from, to))
        }
    }
}