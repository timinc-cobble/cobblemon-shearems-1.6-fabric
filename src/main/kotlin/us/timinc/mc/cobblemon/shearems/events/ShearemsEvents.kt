package us.timinc.mc.cobblemon.shearems.events

import com.cobblemon.mod.common.api.reactive.CancelableObservable
import com.cobblemon.mod.common.api.reactive.EventObservable

object ShearemsEvents {
    @JvmField
    val POKEMON_SHORN_PRE = CancelableObservable<PokemonShorn.Pre>()

    @JvmField
    val POKEMON_SHORN_POST = EventObservable<PokemonShorn.Post>()
}