package moe.paring.simplelots.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.simplelots.persistence.RollableItem
import moe.paring.simplelots.plugin.LotsPlugin
import moe.paring.simplelots.util.PERMISSION_PREFIX
import moe.paring.simplelots.util.rollerArgument
import net.kyori.adventure.text.Component

fun KommandNode.delete() {
    "delete" {
        requires { hasPermission("$PERMISSION_PREFIX.command.delete") }

        then("roller" to rollerArgument) {
            executes { context ->
                val roller: RollableItem by context

                val plugin = LotsPlugin.instance

                plugin.persistence.apply {
                    items.remove(roller.name)
                    save()
                    reindex()
                }

                broadcast(Component.text("Deleted lots ${roller.name}"))
            }
        }
    }
}
