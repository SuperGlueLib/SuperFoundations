package me.superpenguin.superglue.foundations.extensions

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType

fun String.toEntityTypeOrNull() = runCatching { EntityType.valueOf(this) }.getOrNull()
fun String.toMaterialOrNull() = runCatching { Material.matchMaterial(this) }.getOrNull()

/** @return whether the string matches exactly the name of a currently online player */
fun String.isPlayerName() = Bukkit.getPlayerExact(this) != null
fun String.toPlayer() = Bukkit.getPlayerExact(this)

/** Remove all occurences of the provided strings */
fun String.remove(vararg sequences: String) = sequences.fold(this) {str, seq -> str.replace(seq, "") }
/** Remove all occurences of the provided regexes */
fun String.remove(vararg sequences: Regex) = sequences.fold(this) { str, reg -> str.replace(reg, "") }