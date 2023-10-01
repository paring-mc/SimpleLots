package moe.paring.simplelots.plugin

import io.github.monun.kommand.kommand
import moe.paring.simplelots.command.*
import moe.paring.simplelots.listener.InteractionListener
import moe.paring.simplelots.persistence.RollerManager
import moe.paring.simplelots.util.PERMISSION_PREFIX
import org.bukkit.plugin.java.JavaPlugin

class LotsPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: LotsPlugin
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
            register("lots") {
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
