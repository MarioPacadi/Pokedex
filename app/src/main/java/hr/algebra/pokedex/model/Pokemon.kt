package hr.algebra.pokedex.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

data class Pokemon  (
    var _id: Long?,

    var name: String,

    var height: Double,
    var weight: Double,

    var spritePath: String,

    val types: String,
    val moves: String,
    val abilities : String
)
