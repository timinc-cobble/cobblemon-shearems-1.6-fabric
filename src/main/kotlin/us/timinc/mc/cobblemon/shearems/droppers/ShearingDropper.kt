package us.timinc.mc.cobblemon.shearems.droppers

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions
import us.timinc.mc.cobblemon.shearems.Shearems.MOD_ID
import us.timinc.mc.cobblemon.shearems.events.ShearemsEvents

object ShearingDropper : AbstractFormDropper("shearing", MOD_ID) {
    override fun load() {
        ShearemsEvents.POKEMON_SHORN_POST.subscribe { event ->
            val pokemonEntity = event.pokemonEntity
            val level = pokemonEntity.level()
            if (level !is ServerLevel) {
                return@subscribe
            }
            val shornWith = event.tool
            val shornBy = event.player
            val pokemon = event.pokemon

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to pokemonEntity.position(),
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.RELEVANT_PLAYER to shornBy,
                    LootContextParams.TOOL to shornWith
                ),
                mapOf(),
                shornBy?.luck ?: 0F
            )
            val context = FormDropContext(pokemonEntity.pokemon.form)
            val drops = ShearingDropper.getDrops(
                lootParams,
                context
            )

            drops.forEach { drop ->
                val itemEntity = pokemonEntity.spawnAtLocation(drop) ?: return@forEach
                itemEntity.deltaMovement = itemEntity.deltaMovement.add(
                    ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble(),
                    (pokemonEntity.random.nextFloat() * 0.05f).toDouble(),
                    ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble()
                )
            }
        }
    }
}