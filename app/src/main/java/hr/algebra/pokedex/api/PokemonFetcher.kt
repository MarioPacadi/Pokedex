package hr.algebra.pokedex.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.activity.DATA_IMPORTED
import hr.algebra.pokedex.api.response.PokemonDetailsResponse
import hr.algebra.pokedex.framework.setBooleanPreference
import hr.algebra.pokedex.handler.downloadImageAndStore
import hr.algebra.pokedex.handler.getImageUrl
import hr.algebra.pokedex.model.Pokemon
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@DelicateCoroutinesApi
class PokemonFetcher(private val context: Context, private val id: String) {
    private var pokeApi: PokedexApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(POKEAPI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        pokeApi = retrofit.create(PokedexApi::class.java)
    }

    fun fetchItems() {
        val request = pokeApi.getPokemon(id)

        request.enqueue(object: Callback<PokemonDetailsResponse> {
            override fun onResponse(
                call: Call<PokemonDetailsResponse>,
                response: Response<PokemonDetailsResponse>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<PokemonDetailsResponse>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }

        })
    }

    private fun populateItems(details: PokemonDetailsResponse) {
        Log.d(details.name,details.pokemon_id)
        println(details.name+" | "+details.pokemon_id)

        val picturePath = downloadImageAndStore(
            context,
            getImageUrl(details.pokemon_id),
            details.name.replace(" ", "_")
        )

        val values = ContentValues().apply {
            put(Pokemon::name.name, details.name)
            put(Pokemon::weight.name, details.weight)
            put(Pokemon::height.name, details.height)
            put(Pokemon::spritePath.name, picturePath ?: "")
            put(Pokemon::abilities.name, details.abilities.toString())
            put(Pokemon::types.name, details.types.toString())
            put(Pokemon::moves.name, details.moves.toString())
        }

        context.contentResolver.insert(POKEDEX_PROVIDER_URI, values)
        context.setBooleanPreference(DATA_IMPORTED, true)
        //context.sendBroadcast<PokedexReceiver>()
    }
}
