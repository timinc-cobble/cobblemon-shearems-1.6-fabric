@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.shearems

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import us.timinc.mc.cobblemon.shearems.droppers.ShearingDropper
import us.timinc.mc.cobblemon.shearems.events.PokemonShorn
import us.timinc.mc.cobblemon.shearems.events.ShearemsEvents

object Shearems : ModInitializer {
    const val MOD_ID = "shearems"
    val shearingRegistry: MutableMap<PokemonEntity, Pair<ItemStack, ServerPlayer?>> = mutableMapOf()

    override fun onInitialize() {
        ShearingDropper.load()
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
            ?: throw Error("Attempted to complete shearing action for non-registered event for ${pokemonEntity.uuid}")
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