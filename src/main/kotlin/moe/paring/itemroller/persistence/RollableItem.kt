package moe.paring.itemroller.persistence

import org.bukkit.inventory.ItemStack

class RollableItem(
    val name: String
) {
    var items = listOf<ItemStack>()
    var rollerItem: ItemStack? = null
}
