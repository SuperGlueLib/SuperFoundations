package me.superpenguin.superglue.superfoundations

import org.bukkit.ChatColor
import java.util.regex.Matcher

private const val COLOR_CHAR = '\u00A7'
private val ampersandhexregex = "&#([A-Fa-f0-9]{6})".toRegex()
private val hexregex = "(?i)#([a-f0-9]{6})".toRegex()
private val COLOUR_REGEX = "(?i)[&$COLOR_CHAR][0-9a-fk-orx]".toRegex()

/** Provides java Support for optional hex attribute */
fun String.toColour() = toColour(false)

fun String.toColour(hex: Boolean = false) = ChatColor.translateAlternateColorCodes('&', this).let { if (hex) toHexColor(true) else it }
fun String.stripColour() = ChatColor.stripColor(this)!!.remove(COLOUR_REGEX)

// Hex
private fun String.toHexColor(includeAmpersand: Boolean = false): String {
    val matcher: Matcher = (if (includeAmpersand) ampersandhexregex else hexregex).toPattern().matcher(this.toColour())
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