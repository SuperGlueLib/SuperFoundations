package com.github.supergluelib.foundation.util

import com.github.supergluelib.foundation.loadYamlConfiguration
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class YamlFileHandler(private val plugin: JavaPlugin, val name: String, resource: Boolean = false) {
    val file: File = File(plugin.dataFolder, name)
    var config: FileConfiguration

    init {
        if (!file.exists()){
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (resource) plugin.saveResource(name, false)
            else runCatching { file.createNewFile() }
        }
        config = file.loadYamlConfiguration()
        onReload()
    }

    /** Called when the file is first loaded and for every subsequent reload, Use this to update your cache. */
    protected abstract fun onReload()

    fun reload() {
        config = file.loadYamlConfiguration()
        onReload()
    }

    private fun save_() = config.save(file)
    fun save(async: Boolean = false) { if (async) Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable { save_() }) else save_() }
}