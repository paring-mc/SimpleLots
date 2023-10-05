package moe.paring.simplelots.plugin

import io.github.monun.kommand.kommand
import io.github.monun.tap.util.UpToDateException
import io.github.monun.tap.util.updateFromGitHub
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

        logger.info("Checking for updates...")
        updateFromGitHub("pikokr", "SimpleLots", "SimpleLots.jar") {
            onSuccess { logger.info("Update available! Download at https://github.com$it") }
                .onFailure {
                    if (it is UpToDateException) {
                        logger.info("Already up to date.")
                        return@onFailure
                    }
                    logger.info("Failed to check update: $it")
                }
        }

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
