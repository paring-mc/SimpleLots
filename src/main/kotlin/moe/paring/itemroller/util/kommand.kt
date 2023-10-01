package moe.paring.itemroller.util

import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.StringType
import moe.paring.itemroller.plugin.ItemRollerPlugin

val rollerArgument = dynamic(StringType.SINGLE_WORD) { _, input ->
    ItemRollerPlugin.instance.persistence.items[input]
}.apply {
    suggests {
        suggest(ItemRollerPlugin.instance.persistence.items.keys)
    }
}
