package hr.algebra.pokedex.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

data class Pokemon  (
    var _id: Long?,

    var pokedexId : Int,
    var name: String,

    var height: Int,
    var weight: Int,

    var spritePath: String,
    var caught : Boolean,

    val types: String,
    val moves: String,
    val abilities : String
) : Comparable<Pokemon>
{
    override fun compareTo(other: Pokemon): Int {
        return pokedexId.compareTo(other.pokedexId)
    }
}