package hr.algebra.pokedex.api.response.stat

import com.google.gson.annotations.SerializedName
import hr.algebra.pokedex.api.response.PokedexItem

data class Ability(
    @SerializedName("ability")
    val ability: PokedexItem
)
