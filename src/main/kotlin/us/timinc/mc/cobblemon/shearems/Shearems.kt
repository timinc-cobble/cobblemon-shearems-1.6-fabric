@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.shearems

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions
import us.timinc.mc.cobblemon.shearems.droppers.ShearingDropper

object Shearems : ModInitializer {
    const val MOD_ID = "shearems"

    override fun onInitialize() {}

    fun dropShorn(
        pokemonEntity: PokemonEntity,
        shornBy: Player,
        shornWith: ItemStack,
    ) {
        val level = pokemonEntity.level()
        if (level !is ServerLevel) {
            return
        }

        val lootParams = LootParams(
            level,
            mapOf(
                LootContextParams.ORIGIN to pokemonEntity.position(),
                LootContextParams.THIS_ENTITY to pokemonEntity,
                LootConditions.PARAMS.POKEMON_DETAILS to pokemonEntity.pokemon,
                LootContextParams.TOOL to shornWith,
                LootContextParams.DIRECT_ATTACKING_ENTITY to shornBy
            ),
            mapOf(),
            shornBy.luck
        )
        val drops = ShearingDropper.getDrops(
            lootParams,
            FormDropContext(pokemonEntity.pokemon.form)
        )

        drops.forEach { drop ->
            val itemEntity = pokemonEntity.spawnAtLocation(drop) ?: return
            itemEntity.deltaMovement = itemEntity.deltaMovement.add(
                ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble(),
                (pokemonEntity.random.nextFloat() * 0.05f).toDouble(),
                ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble()
            )
        }
    }

    fun modIdentifier(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }
}