package moe.paring.itemroller.command

import io.github.monun.invfx.InvFX
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.KommandNode
import moe.paring.itemroller.persistence.RollableItem
import moe.paring.itemroller.plugin.ItemRollerPlugin
import moe.paring.itemroller.util.PERMISSION_PREFIX
import moe.paring.itemroller.util.rollerArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

fun KommandNode.edit() {
    "edit" {
        requires { hasPermission("$PERMISSION_PREFIX.command.edit") }

        then("roller" to rollerArgument) {
            executes { context ->
                val roller: RollableItem by context

                val pl = ItemRollerPlugin.instance

                val items = roller.items.toMutableList()
                var page = 0
                var maxPage = 0
                val itemsPerPage = (9 * 5)

                var closed = false

                fun refreshPages() {
                    maxPage = max(1, ceil(items.size / itemsPerPage.toFloat()).toInt())
                }

                refreshPages()

                val frame = InvFX.frame(6, Component.text("Edit rewards")) {
                    val itemList = list(0, 0, 8, 4, false, { items }) {
                        onClickItem { x, y, _, _ ->
                            val idx = (y * 9) + x
                            items.removeAt(idx)
                            refresh()
                        }

                        transform {
                            it.clone().apply {
                                itemMeta = itemMeta.apply {
                                    val base = lore()?.toMutableList()?.apply {
                                        add(Component.text(""))
                                    } ?: mutableListOf()

                                    base.add(Component.text("Click to remove"))

                                    lore(base)
                                }
                            }
                        }
                    }

                    fun updatePageText() {
                        page = min(page, maxPage - 1)
                        val currPage = page + 1

                        item(4, 5)?. apply {
                            itemMeta = itemMeta.apply {
                                displayName(Component.text("$currPage / $maxPage"))
                            }
                        }
                    }

                    onClickBottom { ev ->
                        ev.currentItem?.let { items += it.clone() }
                        refreshPages()
                        updatePageText()
                        itemList.refresh()
                    }

                    for (i in 1..7) {
                        if (i == 3) {
                            slot(i, 5) {
                                item = ItemStack(Material.WHITE_STAINED_GLASS_PANE).apply {
                                    itemMeta = itemMeta.apply {
                                        displayName(Component.text("<"))
                                    }
                                }

                                onClick {
                                    page = max(page - 1, 0)
                                    refreshPages()
                                    itemList.index = page * itemsPerPage
                                    updatePageText()
                                    itemList.refresh()
                                }
                            }
                            continue
                        }
                        if (i == 5) {
                            slot(i, 5) {
                                item = ItemStack(Material.WHITE_STAINED_GLASS_PANE).apply {
                                    itemMeta = itemMeta.apply {
                                        displayName(Component.text(">"))
                                    }
                                }

                                onClick {
                                    page = min(page + 1, maxPage - 1)
                                    refreshPages()
                                    itemList.index = page * itemsPerPage
                                    updatePageText()
                                    itemList.refresh()
                                }
                            }
                            continue
                        }
                        item(i, 5, ItemStack(Material.BLUE_STAINED_GLASS_PANE).apply {
                            itemMeta = itemMeta.apply {
                                displayName(Component.text(""))
                            }
                        })
                    }

                    slot(0, 5) {
                        item = ItemStack(Material.RED_STAINED_GLASS_PANE).apply {
                            itemMeta = itemMeta.apply {
                                displayName(Component.text("Cancel"))
                            }
                        }

                        onClick {
                            closed = true
                            player.closeInventory()
                            sender.sendMessage(Component.text("Cancelled.").color(NamedTextColor.RED))
                        }
                    }
                    slot(8, 5) {
                        item = ItemStack(Material.GREEN_STAINED_GLASS_PANE).apply {
                            itemMeta = itemMeta.apply {
                                displayName(Component.text("Save"))
                            }
                        }

                        onClick {
                            closed = true
                            player.closeInventory()
                            roller.items = items.toList()
                            pl.persistence.save()
                            pl.persistence.reindex()

                            broadcast(Component.text("Updated rewards of item roller ${roller.name}."))
                        }
                    }

                    updatePageText()

                    onClose {
                        if (!closed) {
                            roller.items = items.toList()
                            pl.persistence.save()
                            pl.persistence.reindex()

                            broadcast(Component.text("Updated rewards of item roller ${roller.name}."))
                        }
                    }
                }

                player.openFrame(frame)
            }
        }
    }
}
