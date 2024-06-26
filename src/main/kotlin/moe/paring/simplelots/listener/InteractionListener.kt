package moe.paring.simplelots.listener

import moe.paring.simplelots.plugin.LotsPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractionListener(private val plugin: LotsPlugin) : Listener {
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val item = e.item ?: return

        plugin.persistence.rollerByItemStack[item.type]?.let { items ->
            val roller = items.find { it.rollerItem?.itemMeta?.equals(item.itemMeta) ?: false } ?: return
            if (item.amount < roller.rollerItem!!.amount) return
            roller.items.randomOrNull()?.let { reward ->
                e.isCancelled = true
                if (e.player.gameMode != GameMode.CREATIVE) item.amount -= roller.rollerItem!!.amount
                e.player.inventory.addItem(reward).values.forEach {
                    e.player.world.dropItemNaturally(e.player.location, it)
                }
                roller.claimEffectCommands.forEach {
                    plugin.server.dispatchCommand(plugin.server.consoleSender, it.replace("%player%", e.player.name))
                }
                e.player.sendMessage((
                        runCatching { MiniMessage.miniMessage().deserialize(roller.rewardMessage) }.getOrNull()
                            ?: LegacyComponentSerializer.legacyAmpersand()
                                .deserialize(roller.rewardMessage.replace("\\n", "\n")))
                    .replaceText {
                        it.match("%item%").replacement(
                            (reward.itemMeta.displayName()
                                ?: Component.translatable(reward.translationKey())).hoverEvent(reward.asHoverEvent())
                        )
                    }.replaceText {
                        it.match("%amount%").replacement(
                            reward.amount.toString()
                        )
                    })
            }
        }
    }
}
