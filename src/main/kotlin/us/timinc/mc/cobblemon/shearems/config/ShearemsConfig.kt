package us.timinc.mc.cobblemon.shearems.config

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

class ShearemsConfig {
    val tools: Map<String, List<String>> = mapOf(
        "minecraft:shears" to listOf("cobblemon:mareep", "cobblemon:wooloo", "cobblemon:dubwool")
    )

    fun getToolItems(): Set<Item> = tools.entries.fold(setOf()) { acc, (e) ->
        acc.plus(BuiltInRegistries.ITEM.get(ResourceLocation.parse(e)))
    }
}