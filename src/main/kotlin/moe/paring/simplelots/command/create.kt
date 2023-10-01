package moe.paring.simplelots.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.simplelots.plugin.SimpleLogsPlugin
import moe.paring.simplelots.util.PERMISSION_PREFIX
import moe.paring.simplelots.util.executesCatching
import net.kyori.adventure.text.Component

fun KommandNode.create() {
    "create" {
        requires { hasPermission("$PERMISSION_PREFIX.command.create") }

        then("name" to string()) {
            executesCatching { context ->
                val pl = SimpleLogsPlugin.instance

                val name: String by context

                val roller = pl.persistence.create(name)

                broadcast(Component.text("Created lots ${roller.name}."))
            }
        }
    }
}
