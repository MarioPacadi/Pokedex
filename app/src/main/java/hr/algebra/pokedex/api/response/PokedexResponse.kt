package hr.algebra.pokedex.api.response

import com.google.gson.annotations.SerializedName

data class PokedexResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: MutableList<PokedexItem>
)
