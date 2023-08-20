package me.superpenguin.superglue.foundations.extensions

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * load a YML Configuration from a file
 * @return the YML Configuration loaded from the file
 * @see YamlConfiguration.loadConfiguration
 * @throws IllegalArgumentException if the file is null
 */
fun File.getYamlConfiguration() = YamlConfiguration.loadConfiguration(this)