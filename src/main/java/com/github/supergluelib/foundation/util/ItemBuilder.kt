package com.github.supergluelib.foundation.util

import com.github.supergluelib.foundation.*
import com.google.gson.annotations.JsonAdapter
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

/**
 * A kotlin friendly class optimising and shortening the creation of custom itemstacks.
 * This class can be used in conjunction with Gson to serialise ItemStacks.
 */
class ItemBuilder(private var type: Material, Name: String? = null, private var amount: Int? = null) {
    constructor(type: Material, amount: Int?): this(type, null, amount)
    var name: String? = Name
    var lore: ArrayList<String>? = null
    var locname: String? = null
    @JsonAdapter(PersistentDataMapGsonAdapter.IntAdapter::class)
    var persistentInts: HashMap<NamespacedKey, Int>? = null
    @JsonAdapter(PersistentDataMapGsonAdapter.StringAdapter::class)
    var persistentStrings: HashMap<NamespacedKey, String>? = null
    var useHex: Boolean? = null
    @JsonAdapter(EnchantmentMapGsonAdapter::class)
    var enchants: HashMap<Enchantment, Int>? = null
    var hideEnchants: Boolean? = null
    var hideDye: Boolean? = null
    var unbreakable: Boolean? = null
    var glowing: Boolean? = null

    var leathercolor: Color? = null

    var skullowner: UUID? = null

    fun name(name: String) = apply { this.name = name; }
    fun lore(lines: List<String>) = apply { this.lore = ArrayList(lines) }
    fun addLore(vararg line: String) = apply { lore = (lore ?: ArrayList()).apply { addAll(line) } }
    fun addEnchant(enchant: Enchantment, level: Int) = apply { enchants?.put(enchant, level) ?: run { enchants = hashMapOf(enchant to level) } }
    fun enchants(enchants: Map<Enchantment, Int>) = apply { this@ItemBuilder.enchants = enchants.toHashMap() }
    fun hideEnchants(hide: Boolean) = apply { hideEnchants = hide }
    fun locname(locname: String) = apply { this.locname = locname }
    fun addPersistentInt(key: NamespacedKey, data: Int) = apply { persistentInts?.put(key, data) ?: run { persistentInts = hashMapOf(key to data) } }
    fun addPersistentString(key: NamespacedKey, data: String) = apply { persistentStrings?.put(key, data) ?: run { persistentStrings = hashMapOf(key to data) }  }

    fun hex(use: Boolean) = apply { useHex = use }
    fun unbreakable(unbreakable: Boolean) = apply { this.unbreakable = unbreakable }
    fun glowing(glowing: Boolean) = apply { this.glowing = glowing }

    fun leathercolor(color: Color) = apply { leathercolor = color }
    fun hideDye(hide: Boolean) = apply { hideDye = hide }

    fun skullOwner(player: UUID) = apply { skullowner = player }

    fun build(): ItemStack {
        val stack = ItemStack(type, amount ?: 1);
        val meta = stack.itemMeta!!
        if (name != null) meta.setDisplayName(name!!.toColor(useHex ?: false))
        if (lore != null) meta.lore = lore!!.map { it.toColor(useHex ?: false) }
        locname?.let(meta::setLocalizedName)
        unbreakable?.let(meta::setUnbreakable)
        if (persistentInts?.isNotEmpty() == true)
            persistentInts!!.entries.forEach { meta.persistentDataContainer[it.key, PersistentDataType.INTEGER] = it.value }
        if (persistentStrings?.isNotEmpty() == true)
            persistentStrings!!.entries.forEach { meta.persistentDataContainer[it.key, PersistentDataType.STRING] = it.value }
        if (enchants?.isNotEmpty() == true) enchants!!.forEach { (enchant, level) -> meta.addEnchant(enchant, level, true) }
        if (hideEnchants == true || (enchants?.isEmpty() == true && glowing == true)) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        if (glowing == true && enchants?.isEmpty() == true) meta.addEnchant(Enchantment.OXYGEN, 1, false)

        if (meta is LeatherArmorMeta) {
            leathercolor?.let { meta.setColor(it) }
            if (hideDye == true) meta.addItemFlags(ItemFlag.HIDE_DYE)
        }

        if (meta is SkullMeta){
            skullowner?.let { meta.setOwningPlayer(Bukkit.getOfflinePlayer(it)) }
        }

        stack.itemMeta = meta
        return stack
    }

    constructor(item: ItemStack, hex: Boolean? = null): this(item.type, item.amount.takeIf { it != 1 }) {
        val meta = item.itemMeta ?: return
        if (meta.hasLore()) lore(meta.lore!!)
        if (meta.hasLocalizedName()) locname(meta.localizedName)
        if (meta.hasEnchants()) enchants(meta.enchants)
        if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) hideEnchants(true)
        val pdc = meta.persistentDataContainer
        pdc.keys.forEach {
            if (pdc.has(it, PersistentDataType.STRING)) addPersistentString(it, pdc.get(it, PersistentDataType.STRING)!!)
            if (pdc.has(it, PersistentDataType.INTEGER)) addPersistentInt(it, pdc.get(it, PersistentDataType.INTEGER)!!)
        }
        apply { this@ItemBuilder.useHex = hex }
        if (meta.isUnbreakable) unbreakable(true)
        if (meta.enchants.containsKey(Enchantment.OXYGEN) && meta.enchants[Enchantment.OXYGEN] == 1 && meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) glowing(true)
        if (meta is LeatherArmorMeta) {
            leathercolor(meta.color)
            if (meta.hasItemFlag(ItemFlag.HIDE_DYE)) hideDye(true)
        }
        if (meta is SkullMeta) {
            if (meta.owningPlayer != null) skullOwner(meta.owningPlayer!!.uniqueId)
        }
    }

    fun t() {
        val map = hashMapOf("hi" to mutableListOf<String>())
        map.merge("hi", mutableListOf("brandon")) { existing, newvalue ->
            existing.apply { addAll(newvalue) }
        }
    }
}
