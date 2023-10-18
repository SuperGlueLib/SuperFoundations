package com.github.supergluelib.foundation

import org.bukkit.Bukkit

fun async(code: () -> Unit) = Runnables.async(code)
fun runNextTick(code: () -> Unit) = Runnables.runNextTick(code)

/**
 * A class implementing regular bukkit runnables but designed to be kotlin-friendly.
 */
object Runnables {

    private val plugin get() = Foundations.plugin

    fun runNextTick(code: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, code, 0)
    fun runLater(ticks: Long, code: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, code, ticks)

    /**
     * @param interval the time between executions in ticks
     * @param delay the time before the first execution in ticks.
     */
    fun runTimer(interval: Int, delay: Int = 0, code: () -> Unit) = Bukkit.getScheduler().runTaskTimer(plugin, code, delay.toLong(), interval.toLong())

    fun async(code: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(plugin, code)

}