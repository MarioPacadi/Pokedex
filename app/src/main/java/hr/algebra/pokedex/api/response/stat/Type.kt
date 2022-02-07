package hr.algebra.pokedex.api.response.stat

import com.google.gson.annotations.SerializedName
import hr.algebra.pokedex.api.response.PokedexItem

data class Type(
    @SerializedName("type")
    val type: PokedexItem
)