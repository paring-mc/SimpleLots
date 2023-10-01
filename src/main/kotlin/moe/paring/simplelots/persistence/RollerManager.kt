package moe.paring.simplelots.persistence

import moe.paring.simplelots.util.cmdRequire
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class RollerManager(private val file: File) {
    private val config: YamlConfiguration = YamlConfiguration.loadConfiguration(file)

    val items = mutableMapOf<String, RollableItem>()
    var rollerByItemStack = mapOf<Material, List<RollableItem>>()

    fun create(name: String): RollableItem {
        cmdRequire(!items.containsKey(name)) { Component.text("lots already exists") }

        val roller = RollableItem(name)

        items[name] = roller

        reindex()

        return roller
    }

    fun reindex() {
        rollerByItemStack = items.values.filter { it.rollerItem != null }.groupBy {
            it.rollerItem!!.type
        }
    }

    init {
        read()
    }

    private fun read() {
        config.getMapList("rollers").forEach { map ->
            val roller = RollableItem(map["name"] as String)
            roller.rollerItem = map["rollerItem"] as ItemStack?
            val items = map["rewards"] as List<*>

            roller.items = items.map { (it as ItemStack).clone() }

            roller.claimEffectCommands = (map["claimEffectCommands"] as List<*>? ?: listOf<String>()).map { it as String }

            this@RollerManager.items[roller.name] = roller
        }

        reindex()
    }

    fun save() {
        config.set("rollers", items.values.map {
            mapOf(
                "name" to it.name,
                "rollerItem" to it.rollerItem,
                "rewards" to it.items,
                "claimEffectCommands" to it.claimEffectCommands
            )
        })

        config.save(file)
    }
}