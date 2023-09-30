package moe.paring.itemroller.plugin

import io.github.monun.kommand.kommand
import moe.paring.itemroller.command.guiTest
import org.bukkit.plugin.java.JavaPlugin

class ItemRollerPlugin : JavaPlugin() {
    override fun onEnable() {
        registerCommands()
    }

    private fun registerCommands() {
        kommand {
            register("itemroller", "ir") {
                guiTest()
            }
        }
    }
}
