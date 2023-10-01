package moe.paring.itemroller.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.itemroller.plugin.ItemRollerPlugin
import moe.paring.itemroller.util.PERMISSION_PREFIX
import moe.paring.itemroller.util.executesCatching
import net.kyori.adventure.text.Component

fun KommandNode.create() {
    "create" {
        requires { hasPermission("$PERMISSION_PREFIX.command.create") }

        then("name" to string()) {
            executesCatching { context ->
                val pl = ItemRollerPlugin.instance

                val name: String by context

                val roller = pl.persistence.create(name)

                broadcast(Component.text("Created item roller ${roller.name}."))
            }
        }
    }
}
