package us.timinc.mc.cobblemon.shearems.events

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

interface PokemonShorn {
    val pokemon: Pokemon
    val pokemonEntity: PokemonEntity
    val tool: ItemStack
    val player: ServerPlayer?

    class Pre(
        override val pokemon: Pokemon,
        override val pokemonEntity: PokemonEntity,
        override val tool: ItemStack,
        override val player: ServerPlayer?
    ) : PokemonShorn, Cancelable()

    class Post(
        override val pokemon: Pokemon,
        override val pokemonEntity: PokemonEntity,
        override val tool: ItemStack,
        override val player: ServerPlayer?
    ) : PokemonShorn
}