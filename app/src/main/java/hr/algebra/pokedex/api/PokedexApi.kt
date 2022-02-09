package hr.algebra.pokedex.api

import hr.algebra.pokedex.api.response.PokedexItem
import hr.algebra.pokedex.api.response.PokedexResponse
import hr.algebra.pokedex.api.response.PokemonDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val POKEAPI_URL: String = "https://pokeapi.co/api/v2/"
interface PokedexApi {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<PokemonDetailsResponse>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name: String?): Call<PokemonDetailsResponse>

    @GET("pokemon")
    fun fetchPokemon(
        @Query("offset") offset : Int  = 0,
        @Query("limit") limit: Int = 151
    ): Call<PokedexResponse>
}