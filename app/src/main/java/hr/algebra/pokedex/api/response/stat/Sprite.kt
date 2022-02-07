package hr.algebra.pokedex.api.response.stat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Sprite (
    @SerializedName("front_default")
    val frontDefault: String?,

    @SerializedName("back_default")
    val backDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("back_shiny")
    val backShiny: String?,

    @SerializedName("back_female")
    val backFemale: Any?,
    @SerializedName("back_shiny_female")
    val backShinyFemale: Any?,
    @SerializedName("front_female")
    val frontFemale: Any?,
    @SerializedName("front_shiny_female")
    val frontShinyFemale: Any?
)
