package moe.paring.simplelots.command

import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.simplelots.persistence.RollableItem
import moe.paring.simplelots.plugin.LotsPlugin
import moe.paring.simplelots.util.rollerArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

fun KommandNode.config() {
    val plugin = LotsPlugin.instance

    "config" {
        then("roller" to rollerArgument) {
            "claimEffectCommand" {
                executes { context ->
                    val roller: RollableItem by context
                    sender.sendMessage(
                        Component.text("Current effect command value of ${roller.name} is ")
                            .append(
                                Component.text(roller.claimEffectCommands.toString())
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to copy")))
                                    .clickEvent(ClickEvent.copyToClipboard(roller.claimEffectCommands.joinToString(";;;")))
                                    .color(NamedTextColor.GRAY)
                            )
                            .appendNewline()
                            .append(Component.text("If you want to remove this value, Please use _reset"))
                    )
                }

                then("value" to string(StringType.GREEDY_PHRASE)) {
                    executes { context ->
                        val roller: RollableItem by context
                        val value: String by context

                        if (value == "_reset") {
                            roller.claimEffectCommands = listOf()
                        } else {
                            roller.claimEffectCommands = value.split(";;;")
                        }

                        plugin.persistence.save()
                        broadcast(Component.text("Changed effect command of ${roller.name}"))
                    }
                }
            }
            "rewardMessage" {
                executes { context ->
                    val roller: RollableItem by context
                    sender.sendMessage(
                        Component.text("Current reward message of ${roller.name} is ")
                            .append(
                                Component.text(roller.rewardMessage)
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to copy")))
                                    .clickEvent(ClickEvent.copyToClipboard(roller.rewardMessage))
                                    .color(NamedTextColor.GRAY)
                            )
                    )
                }

                then("value" to string(StringType.GREEDY_PHRASE)) {
                    executes { context ->
                        val roller: RollableItem by context
                        val value: String by context

                        roller.rewardMessage = value

                        plugin.persistence.save()
                        broadcast(Component.text("Changed reward message of ${roller.name}"))
                    }
                }
            }
        }
    }
}
