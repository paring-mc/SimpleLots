package moe.paring.simplelots.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.simplelots.persistence.RollableItem
import moe.paring.simplelots.plugin.SimpleLogsPlugin
import moe.paring.simplelots.util.PERMISSION_PREFIX
import moe.paring.simplelots.util.cmdRequire
import moe.paring.simplelots.util.executesCatching
import moe.paring.simplelots.util.rollerArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

fun KommandNode.setItem() {
    "setItem" {
        requires { hasPermission("$PERMISSION_PREFIX.command.setItem") }

        then("roller" to rollerArgument) {
            executesCatching { context ->
                val roller: RollableItem by context
                val item = player.inventory.itemInMainHand.clone()

                cmdRequire(item.type != Material.AIR) { Component.text("Please hold item to set") }

                roller.rollerItem = item

                val itemComponent = Component.text()
                    .append(Component.text("["))
                    .append(Component.translatable(item.translationKey()))
                    .append(Component.text(" x${item.amount}]"))
                    .hoverEvent(item.asHoverEvent())
                    .decoration(TextDecoration.ITALIC, true)

                SimpleLogsPlugin.instance.persistence.apply {
                    save()
                    reindex()
                }

                broadcast(Component.text("Changed item of roller ${roller.name} to ")
                    .append(itemComponent))
            }
        }
    }
}