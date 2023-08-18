package me.superpenguin.superglue.foundations.util

import me.superpenguin.superglue.foundations.getYamlConfiguration
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class FileHandler(private val plugin: JavaPlugin, val name: String, resource: Boolean = false) {
    val file: File = File(plugin.dataFolder, name)
    var config: FileConfiguration

    init {
        if (!file.exists()){
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (resource) plugin.saveResource(name, false)
            else file.runCatching { createNewFile() }
        }
        config = file.getYamlConfiguration()
    }

    protected abstract fun onReload()

    fun reload() {
        config = file.getYamlConfiguration()
        onReload()
    }

    private fun save_() = config.save(file)
    fun save(async: Boolean = false) { if (async) Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable { save_() }) else save_() }
}