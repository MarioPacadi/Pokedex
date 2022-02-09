package hr.algebra.pokedex.api.response

import com.google.gson.annotations.SerializedName


data class PokedexItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
