package hr.algebra.pokedex.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.activity.DATA_IMPORTED
import hr.algebra.pokedex.PokedexReceiver
import hr.algebra.pokedex.api.response.PokedexResponse
import hr.algebra.pokedex.api.response.PokemonDetailsResponse
import hr.algebra.pokedex.framework.sendBroadcast
import hr.algebra.pokedex.framework.setBooleanPreference
import hr.algebra.pokedex.handler.downloadImageAndStore
import hr.algebra.pokedex.handler.getImageUrl
import hr.algebra.pokedex.model.Pokemon
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@DelicateCoroutinesApi
class PokedexFetcher(private val context: Context) {
    private var pokeApi: PokedexApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(POKEAPI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        pokeApi = retrofit.create(PokedexApi::class.java)
    }

    private lateinit var pokemons : MutableList<ContentValues>

    fun fetchItems() {
        val request = pokeApi.fetchPokemon(0,3)

        request.enqueue(object: Callback<PokedexResponse> {
            override fun onResponse(
                call: Call<PokedexResponse>,
                response: Response<PokedexResponse>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<PokedexResponse>, t: Throwable) {
               Log.e(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(pokedexResponses: PokedexResponse) {
        pokemons= mutableListOf()
        GlobalScope.launch {
            pokedexResponses.results.forEach {
                fetchPokemon(it.name)
            }
            Log.w("PokedexFetcher", "Fetched all pokemon")
            pokemons.forEach {
                context.contentResolver.insert(POKEDEX_PROVIDER_URI, it)
            }
            Log.w("PokedexFetcher", "Inserted all values to context")

            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<PokedexReceiver>()
        }
    }

    private fun fetchPokemon(id : String) {
        val request = pokeApi.getPokemon(id)

        request.enqueue(object: Callback<PokemonDetailsResponse> {
            override fun onResponse(
                call: Call<PokemonDetailsResponse>,
                response: Response<PokemonDetailsResponse>
            ) {
                    response.body()?.let { details ->
                        GlobalScope.launch {
                        Log.d(details.name,details.pokemon_id)
                        println(details.name+" | "+details.pokemon_id)

                        val picturePath = downloadImageAndStore(
                            context,
                            getImageUrl(details.pokemon_id),
                            details.name.replace(" ", "_")
                        )

//                        val picturePath = downloadImageAndStore(
//                            context,
//                            details.sprites.frontDefault.toString(),
//                            details.name.replace(" ", "_")
//                        )

                        val values = ContentValues().apply {
                            put(Pokemon::name.name, details.name)
                            put(Pokemon::weight.name, details.weight)
                            put(Pokemon::height.name, details.height)
                            put(Pokemon::spritePath.name, picturePath ?: "")
                            put(Pokemon::abilities.name, details.getStringOfAbilities())
                            put(Pokemon::types.name, details.getStringOfTypes())
                            put(Pokemon::moves.name, details.getStringOfMoves())
                        }
                        pokemons.add(values)
                    }
                }
            }

            override fun onFailure(call: Call<PokemonDetailsResponse>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }

        })
    }

}