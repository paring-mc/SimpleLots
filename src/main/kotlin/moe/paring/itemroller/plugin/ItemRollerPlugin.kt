package moe.paring.itemroller.plugin

import io.github.monun.kommand.kommand
import moe.paring.itemroller.command.*
import moe.paring.itemroller.listener.InteractionListener
import moe.paring.itemroller.persistence.RollerManager
import moe.paring.itemroller.util.PERMISSION_PREFIX
import org.bukkit.plugin.java.JavaPlugin

class ItemRollerPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: ItemRollerPlugin
    }

    lateinit var persistence: RollerManager

    override fun onEnable() {
        instance = this

        persistence = RollerManager(
            dataFolder.resolve("data.yml")
        )

        server.pluginManager.registerEvents(InteractionListener(this), this)

        registerCommands()
    }

    override fun onDisable() {
        persistence.save()
    }

    private fun registerCommands() {
        kommand {
            register("itemroller", "ir") {
                requires { isPlayer && hasPermission("$PERMISSION_PREFIX.command") }

                edit()
                setItem()
                create()
                delete()
                config()
            }
        }
    }
}
