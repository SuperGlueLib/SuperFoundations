package com.github.supergluelib.foundation.util

import com.github.supergluelib.foundation.toColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

class ItemBuilder(private var type: Material, Name: String? = null, private var amount: Int = 1) {
    var name: String? = Name
    var lore: ArrayList<String>? = null
    var locname: String? = null
    var persistentInts: ArrayList<Pair<NamespacedKey, Int>> = ArrayList()
    var persistentStrings: ArrayList<Pair<NamespacedKey, String>> = ArrayList()
    var useHex = false
    var enchants: HashMap<Enchantment, Int> = HashMap()
    var hideEnchants: Boolean = false
    var hideDye: Boolean = false
    var unbreakable: Boolean? = null
    var glowing: Boolean? = null

    var leathercolor: Color? = null

    var skullowner: UUID? = null

    fun name(name: String) = apply { this.name = name; }
    fun lore(lines: List<String>) = apply { this.lore = ArrayList(lines) }
    fun addLore(vararg line: String) = apply { lore = (lore ?: ArrayList()).apply { addAll(line) } }
    fun addEnchant(enchant: Enchantment, level: Int) = apply { enchants[enchant] = level }
    fun hideEnchants(hide: Boolean) = apply { hideEnchants = hide }
    fun locname(locname: String) = apply { this.locname = locname }
    fun addPersistentInt(key: NamespacedKey, data: Int) = apply { persistentInts.add(key to data) }
    fun addPersistentString(key: NamespacedKey, data: String) = apply { persistentStrings.add(key to data) }

    fun hex(use: Boolean) = apply { useHex = use }
    fun unbreakable(unbreakable: Boolean) = apply { this.unbreakable = unbreakable }
    fun glowing(glowing: Boolean) = apply { this.glowing = glowing }

    fun leathercolor(color: Color) = apply { leathercolor = color }
    fun hideDye(hide: Boolean) = apply { hideDye = hide }

    fun skullOwner(player: UUID) = apply { skullowner = player }

    fun build(): ItemStack {
        val stack = ItemStack(type, amount);
        val meta = stack.itemMeta!!
        if (name != null) meta.setDisplayName(name!!.toColor(useHex))
        if (lore != null) meta.lore = lore!!.map { it.toColor(useHex) }
        locname?.let(meta::setLocalizedName)
        unbreakable?.let(meta::setUnbreakable)
        if (persistentInts.isNotEmpty()) persistentInts.forEach { meta.persistentDataContainer[it.first, PersistentDataType.INTEGER] = it.second }
        if (persistentStrings.isNotEmpty()) persistentStrings.forEach { meta.persistentDataContainer[it.first, PersistentDataType.STRING] = it.second }
        if (enchants.isNotEmpty()) enchants.forEach { (enchant, level) -> meta.addEnchant(enchant, level, true) }
        if (hideEnchants || (enchants.isEmpty() && glowing == true)) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (glowing == true && enchants.isEmpty()) meta.addEnchant(Enchantment.OXYGEN, 1, false)

        if (meta is LeatherArmorMeta) {
            leathercolor?.let { meta.setColor(it) }
            if (hideDye) meta.addItemFlags(ItemFlag.HIDE_DYE)
        }

        if (meta is SkullMeta){
            skullowner?.let { meta.setOwningPlayer(Bukkit.getOfflinePlayer(it)) }
        }

        stack.itemMeta = meta
        return stack
    }
}
