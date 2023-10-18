package com.github.supergluelib.foundation

import com.github.supergluelib.foundation.customevents.CustomEventListener
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.File
import java.util.*
import kotlin.collections.HashMap

object Foundations {
    private var _plugin: JavaPlugin? = null
    val plugin: JavaPlugin get() {
        if (_plugin == null) throw NullPointerException("""
            You must call Foundations#setup(plugin) before you use this class, usually in the onEnable
            and Foundations#onDisable() after you've finished using it, usually in the onDisable()
        """.trimIndent())
        else return _plugin!!
    }

    fun setup(plugin: JavaPlugin) {
        this._plugin = plugin
        CustomEventListener.setup(plugin)
    }

    fun onDisable() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord")
    }
}

/** Send a coloured message to a player */
fun CommandSender.send(msg: String, hex: Boolean = false) = sendMessage(msg.toColor(hex))

// A bunch of useful string methods.
fun String.toIntOrElse(default: Int): Int = this.toIntOrNull() ?: default
fun String.isInt() = toIntOrNull() != null
fun String.toEntityTypeOrNull() = runCatching { EntityType.valueOf(this) }.getOrNull()
fun String.toMaterialOrNull() = Material.matchMaterial(this)
fun String.toUUID() = runCatching { UUID.fromString(this) }.getOrNull()
/** @return whether the string matches exactly the name of a currently online player */
fun String.isPlayerName() = Bukkit.getPlayerExact(this) != null
fun String.toPlayer() = Bukkit.getPlayerExact(this)

/** load a YML Configuration from a file */
fun File.getYamlConfiguration() = YamlConfiguration.loadConfiguration(this)

/** Remove all occurences of the provided strings */
fun String.remove(vararg sequences: String) = sequences.fold(this) {str, seq -> str.replace(seq, "") }
/** Remove all occurences of the provided regexes */
fun String.remove(vararg sequences: Regex) = sequences.fold(this) { str, reg -> str.replace(reg, "") }

// ItemStacks
/** Decrement the amount of the itemstack by 1 */
fun ItemStack.removeOne() { amount -= 1 }
/** @return true if the itemstack is not null and not air */
fun ItemStack?.isValid() = (this != null) && !type.isAir

// Map & List utils
/** @return whether the collection contains the provided string, ignoring case */
fun Iterable<String>.containsIgnoreCase(other: String) = any { other.equals(it, true) }
/** @return whether the array contains the provided string, ignoring case */
fun Array<out String>.containsIgnoreCase(other: String) = any { other.equals(it, true)}
/** @return a list containing the elements with indices within the supplied range */
operator fun Array<out Any>.get(range: IntRange) = this.withIndex().filter { range.contains(it.index) }
/** @return a subset of this array from the begin index to the end index */
fun Array<out Any>.sublist(begin: Int, end: Int = size) = Array(end-begin) { this[it + begin] }
/** Inline method to convert a map to a hashmap */
fun <K, V> Map<K, V>.toHashMap() = HashMap(this)

/** Easy Listener registering :D */
fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)
/** Easy mass listener registering */
fun JavaPlugin.registerListeners(vararg listeners: Listener) = Bukkit.getPluginManager().let { manager -> listeners.forEach { manager.registerEvents(it, this@registerListeners) } }

/** Checks if player click was in top inventory not bottom inventory */
fun InventoryClickEvent.clickedTopInventory() = clickedInventory?.equals(view.topInventory) == true

/** Adds the item to a players inventory and drops any items that didn't fit onto the floor */
fun Player.giveOrDropItem(item: ItemStack) = inventory.addItem(item).forEach { (_, item) ->  world.dropItem(location, item)}

/** Get the top-center of the block represented by this location */
fun Location.toCenter() = Location(world, blockX + 0.5, blockY + 0.5, blockZ + 0.5, yaw, pitch)

object PluginMessager {

    init {
        Bukkit.getMessenger().registerOutgoingPluginChannel(Foundations.plugin, "BungeeCord")
    }

    /** Sends the player to the bungeecord server specified by [servername] or prints a warning to console if it is not found */
    fun Player.connectToBungeeServer(servername: String) = runCatching {
        val output = ByteStreams.newDataOutput().apply {
            writeUTF("Connect")
            writeUTF(servername)
        }.toByteArray()
        sendPluginMessage(Foundations.plugin, "BungeeCord", output)
    }.onFailure { Foundations.plugin.logger.warning("Failed to send $name to bungee server $servername, check the spelling and that the server is online") }
}

