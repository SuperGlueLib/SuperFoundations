package me.superpenguin.superglue.foundations.extensions

import org.bukkit.ChatColor
import java.util.regex.Matcher

private const val COLOR_CHAR = '\u00A7'
private val ampersAndHexRegex = "&#([A-Fa-f0-9]{6})".toRegex()
private val hexRegex = "(?i)#([a-f0-9]{6})".toRegex()
private val COLOR_REGEX = "(?i)[&$COLOR_CHAR][0-9a-fk-orx]".toRegex()

/** Provides java Support for optional hex attribute */
fun String.toColor() = toColor(false)

fun String.toColor(hex: Boolean = false) = ChatColor.translateAlternateColorCodes('&', this).let { if (hex) toHexColor(true) else it }
fun String.stripColor() = ChatColor.stripColor(this)!!.remove(COLOR_REGEX)

// Hex
private fun String.toHexColor(includeAmpersand: Boolean = false): String {
    val matcher: Matcher = (if (includeAmpersand) ampersAndHexRegex else hexRegex).toPattern().matcher(this.toColor())
    val buffer = StringBuffer(length + 4 * 8)
    while (matcher.find()) {
        val group: String = matcher.group(1)
        matcher.appendReplacement(
            buffer, COLOR_CHAR.toString() + "x"
                    + COLOR_CHAR + group[0] + COLOR_CHAR + group[1]
                    + COLOR_CHAR + group[2] + COLOR_CHAR + group[3]
                    + COLOR_CHAR + group[4] + COLOR_CHAR + group[5]
        )
    }
    return matcher.appendTail(buffer).toString()
}