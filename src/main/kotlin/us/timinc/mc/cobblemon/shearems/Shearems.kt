@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.shearems

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.DispenserBlock
import us.timinc.mc.cobblemon.shearems.config.ShearemsConfig
import us.timinc.mc.cobblemon.shearems.droppers.ShearingDropper
import us.timinc.mc.cobblemon.shearems.events.PokemonShorn
import us.timinc.mc.cobblemon.shearems.events.ShearemsEvents
import us.timinc.mc.cobblemon.unchained.config.ConfigBuilder

object Shearems : ModInitializer {
    const val MOD_ID = "shearems"
    val shearingRegistry: MutableMap<PokemonEntity, Pair<ItemStack, ServerPlayer?>> = mutableMapOf()
    var config: ShearemsConfig = ConfigBuilder.load(ShearemsConfig::class.java, MOD_ID)

    override fun onInitialize() {
        ShearingDropper.load()
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register { _, _, _ ->
            config = ConfigBuilder.load(ShearemsConfig::class.java, MOD_ID)
        }
        for (toolItem in config.getToolItems()) {
            DispenserBlock.DISPENSER_REGISTRY[toolItem] = ShearsDispenseItemBehavior()
        }
    }

    fun dispenserShearing(pokemonEntity: PokemonEntity, itemStack: ItemStack) {
        shearingRegistry[pokemonEntity] = itemStack to null
    }

    fun interactShearing(pokemonEntity: PokemonEntity, itemStack: ItemStack, player: ServerPlayer) {
        shearingRegistry[pokemonEntity] = itemStack to player
    }

    fun pokemonShorn(
        pokemonEntity: PokemonEntity,
    ) {
        val (tool, player) = shearingRegistry[pokemonEntity]
            ?: return
        ShearemsEvents.POKEMON_SHORN_PRE.postThen(
            PokemonShorn.Pre(
                pokemonEntity.pokemon,
                pokemonEntity,
                tool,
                player
            ),
            { },
            {
                ShearemsEvents.POKEMON_SHORN_POST.post(
                    PokemonShorn.Post(
                        pokemonEntity.pokemon,
                        pokemonEntity,
                        tool,
                        player
                    )
                )
            },
        )
        shearingRegistry.remove(pokemonEntity)
    }

    fun modIdentifier(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }
}