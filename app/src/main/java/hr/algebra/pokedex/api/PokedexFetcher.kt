package hr.algebra.pokedex.api

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import hr.algebra.pokedex.ITEMS
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.activity.DATA_IMPORTED
import hr.algebra.pokedex.PokedexReceiver
import hr.algebra.pokedex.activity.DELAY
import hr.algebra.pokedex.api.response.PokedexResponse
import hr.algebra.pokedex.api.response.PokemonDetailsResponse
import hr.algebra.pokedex.framework.callDelayed
import hr.algebra.pokedex.framework.sendBroadcast
import hr.algebra.pokedex.framework.setBooleanPreference
import hr.algebra.pokedex.handler.downloadImageAndStore
import hr.algebra.pokedex.handler.getImageUrl
import hr.algebra.pokedex.model.Pokemon
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
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
        val request = pokeApi.fetchPokemon(0, ITEMS)

        request.enqueue(object : Callback<PokedexResponse> {
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

    private fun fetchPokemon(id : String) {
        val request = pokeApi.getPokemon(id)

        request.enqueue(object: Callback<PokemonDetailsResponse> {
            @RequiresApi(Build.VERSION_CODES.R)
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

    private fun populateItems(pokedexResponses: PokedexResponse) {
        pokemons= mutableListOf()
        pokedexResponses.results.forEach {
            fetchPokemon(it.name)
        }
        Log.w("PokedexFetcher", "Fetched all pokemon")
        Log.w("PokedexFetcher", "Inserted all values to context")

        callDelayed(DELAY) {
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<PokedexReceiver>()
        }
        Log.d("PokedexFetcher", "Pokemons content values is empty: ${pokemons.isEmpty()}")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun populateItems(details: PokemonDetailsResponse) {
        GlobalScope.launch {
            //Log.d("PokemonInfo",details.toString())

            var picturePath = downloadImageAndStore(
                context,
                getImageUrl(details.pokemon_id),
                details.name.replace(" ", "_")
            )

            if (picturePath.isNullOrEmpty()){
                picturePath = downloadImageAndStore(
                    context,
                    details.sprites.frontDefault.toString(),
                    details.name.replace(" ", "_")
                )
            }

            val values = ContentValues().apply {
                put(Pokemon::pokedexId.name, details.pokemon_id) //sort order
                put(Pokemon::name.name, details.name)
                put(Pokemon::weight.name, details.weight)
                put(Pokemon::height.name, details.height)
                put(Pokemon::spritePath.name, picturePath ?: "")
                put(Pokemon::caught.name,false)
                put(Pokemon::abilities.name, details.getStringOfAbilities())
                put(Pokemon::types.name, details.getStringOfTypes())
                put(Pokemon::moves.name, details.getStringOfMoves())
            }
            //Log.d("ContentValues","Values is empty: ${values.isEmpty}")
            context.contentResolver.insert(POKEDEX_PROVIDER_URI, values)
            pokemons.add(values)
        }
    }

}