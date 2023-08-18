package me.superpenguin.superglue.foundations

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * A class implementing regular bukkit runnables but designed to be kotlin-friendly.
 */
object Runnables {

    private lateinit var plugin: JavaPlugin
    private fun getPlugin() = runCatching { plugin }.getOrElse { throw UninitializedPropertyAccessException("You must use Runnables.setup() in the onEnable before using this class") }

    fun setup(plugin: JavaPlugin) {
        this.plugin = plugin
    }

    fun runNextTick(code: () -> Unit) = Bukkit.getScheduler().runTaskLater(getPlugin(), code, 0)
    fun runLater(ticks: Long, code: () -> Unit) = Bukkit.getScheduler().runTaskLater(getPlugin(), code, ticks)

    /**
     * @param interval the time between executions in ticks
     * @param delay the time before the first execution in ticks.
     */
    fun runTimer(interval: Int, delay: Int = 0, code: () -> Unit) = Bukkit.getScheduler().runTaskTimer(getPlugin(), code, delay.toLong(), interval.toLong())

}