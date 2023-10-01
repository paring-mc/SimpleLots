package moe.paring.itemroller.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.itemroller.persistence.RollableItem
import moe.paring.itemroller.plugin.ItemRollerPlugin
import moe.paring.itemroller.util.PERMISSION_PREFIX
import moe.paring.itemroller.util.rollerArgument
import net.kyori.adventure.text.Component

fun KommandNode.delete() {
    "delete" {
        requires { hasPermission("$PERMISSION_PREFIX.command.delete") }

        then("roller" to rollerArgument) {
            executes { context ->
                val roller: RollableItem by context

                val plugin = ItemRollerPlugin.instance

                plugin.persistence.apply {
                    items.remove(roller.name)
                    save()
                    reindex()
                }

                broadcast(Component.text("Deleted item roller ${roller.name}"))
            }
        }
    }
}
