@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.shearems

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.fabricmc.api.ModInitializer
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

object Shearems : ModInitializer {
    const val MOD_ID = "shearems"

    override fun onInitialize() {}

    fun dropShorn(pokemonEntity: PokemonEntity) {
        val world = pokemonEntity.world
        if (world !is ServerWorld) {
            return
        }
        val drops = getDrops(
            world,
            pokemonEntity.pos,
            pokemonEntity.pokemon,
            "pokeshear"
        )
        drops.forEach { drop ->
            val itemEntity = pokemonEntity.dropStack(drop) ?: return
            itemEntity.velocity = itemEntity.velocity.add(
                ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble(),
                (pokemonEntity.random.nextFloat() * 0.05f).toDouble(),
                ((pokemonEntity.random.nextFloat() - pokemonEntity.random.nextFloat()) * 0.1f).toDouble()
            )
        }
    }

    fun getDrops(
        world: ServerWorld,
        position: Vec3d,
        pokemon: Pokemon,
        dropType: String,
    ): List<ItemStack> {
        val lootManager = world.server.reloadableRegistries.getIds(RegistryKeys.LOOT_TABLE)

        val lootContextParameterSet = LootContextParameterSet(
            world, mapOf(
                LootContextParameters.ORIGIN to position,
                LootContextParameters.THIS_ENTITY to pokemon.entity
            ), mapOf(), 0F
        )

        val results: MutableList<ItemStack> = mutableListOf()
        val speciesDropId =
            modIdentifier("gameplay/$dropType/species/${pokemon.species.resourceIdentifier.path}")
        if (lootManager.contains(speciesDropId)) {
            val lootTable =
                world.server.reloadableRegistries.getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, speciesDropId))
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        val globalDropId = modIdentifier("gameplay/$dropType/all")
        if (lootManager.contains(globalDropId)) {
            val lootTable =
                world.server.reloadableRegistries.getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, globalDropId))
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        return results
    }

    fun modIdentifier(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }
}