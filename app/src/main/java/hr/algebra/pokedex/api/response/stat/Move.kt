package hr.algebra.pokedex.api.response.stat

import com.google.gson.annotations.SerializedName
import hr.algebra.pokedex.api.response.PokedexItem

data class Move(
    @SerializedName("move")
    val move: PokedexItem
    )
