package moe.paring.simplelots.util

import io.github.monun.kommand.KommandArgument.Companion.dynamic
import io.github.monun.kommand.StringType
import moe.paring.simplelots.plugin.SimpleLogsPlugin

val rollerArgument = dynamic(StringType.SINGLE_WORD) { _, input ->
    SimpleLogsPlugin.instance.persistence.items[input]
}.apply {
    suggests {
        suggest(SimpleLogsPlugin.instance.persistence.items.keys)
    }
}
