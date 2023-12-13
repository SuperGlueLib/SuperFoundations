package com.github.supergluelib.foundation

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.enchantments.Enchantment

@RequiresOptIn("This Gson adapter is new and may be susecptible to bugs and errors, please report any issues you find", RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ExperimentalGsonAdapter

/**
 * Registering custom type adapters sometimes requires different methods, this ensures they will always register correctly.
 */
fun GsonBuilder.registerCustomTypeAdapter(adapter: CustomAdapter<*>) = adapter.register(this)

abstract class CustomAdapter<T>: TypeAdapter<T>(){
    abstract fun register(gson: GsonBuilder): GsonBuilder
    abstract fun readIn(reader: JsonReader): T?
    abstract fun writeOut(value: T, writer: JsonWriter)

    override fun write(out: JsonWriter, value: T?) {
        if (value == null) out.nullValue()
        else writeOut(value, out)
    }

    override fun read(input: JsonReader): T? {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        } else return readIn(input)
    }
}

@ExperimentalGsonAdapter
class WorldGsonAdapter(): CustomAdapter<World>() {
    override fun register(gson: GsonBuilder) = gson.registerTypeHierarchyAdapter(World::class.java, this)
    override fun readIn(reader: JsonReader) = Bukkit.getWorld(reader.nextString())
    override fun writeOut(value: World, writer: JsonWriter) { writer.value(value.name) }
}

@ExperimentalGsonAdapter
class BlockLocationAdapter(val world: World? = null): CustomAdapter<Location>() {
    override fun register(gson: GsonBuilder): GsonBuilder = gson.registerTypeAdapter(Location::class.java, this)
    private fun JsonReader.nextNameAndInt(): Int { nextName(); return nextInt() }
    override fun readIn(reader: JsonReader): Location {
        reader.beginObject()
        val x = reader.nextNameAndInt()
        val y = reader.nextNameAndInt()
        val z = reader.nextNameAndInt()
        reader.endObject()
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }
    override fun writeOut(value: Location, writer: JsonWriter) {
        writer.beginObject()
            .name("x").value(value.blockX)
            .name("y").value(value.blockY)
            .name("z").value(value.blockZ)
            .endObject()
    }
}

class EnchantmentMapGsonAdapter(): CustomAdapter<Map<Enchantment, Int>>() {
    private val token = object: TypeToken<Map<Enchantment, Int>>(){}
    private fun String.toEnchantByKey() = Enchantment.getByKey(NamespacedKey.minecraft(this))
    override fun register(gson: GsonBuilder) = gson.registerTypeHierarchyAdapter(token.rawType, this)
    override fun readIn(reader: JsonReader): Map<Enchantment, Int> {
        val enchants = mutableMapOf<Enchantment, Int>()
        reader.beginArray()
        while (reader.peek() == JsonToken.BEGIN_OBJECT) {
            reader.beginObject()
            val enchant = reader.nextName().toEnchantByKey()!!
            val level = reader.nextInt()
            reader.endObject()
            enchants[enchant] = level
        }
        reader.endArray()
        return enchants
    }
    override fun writeOut(value: Map<Enchantment, Int>, writer: JsonWriter) {
        writer.beginArray()
        for (entry in value.entries) {
            writer.beginObject()
            writer.name(entry.key.key.key).value(entry.value)
            writer.endObject()
        }
        writer.endArray()
    }
}