package moe.paring.simplelots.util

import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.StringType
import moe.paring.simplelots.plugin.LotsPlugin

val rollerArgument = dynamic(StringType.SINGLE_WORD) { _, input ->
    LotsPlugin.instance.persistence.items[input]
}.apply {
    suggests {
        suggest(LotsPlugin.instance.persistence.items.keys)
    }
}
