package hr.algebra.pokedex.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.pokedex.DATA_IMPORTED
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.PokedexReceiver
import hr.algebra.pokedex.framework.sendBroadcast
import hr.algebra.pokedex.framework.setBooleanPreference
import hr.algebra.pokedex.handler.downloadImageAndStore
import hr.algebra.pokedex.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedexFetcher(private val context: Context) {
    private var nasaApi: NasaApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retrofit.create(NasaApi::class.java)
    }

    fun fetchItems() {
        val request = nasaApi.fetchItems()

        request.enqueue(object: Callback<List<PokedexItem>> {
            override fun onResponse(
                call: Call<List<PokedexItem>>,
                response: Response<List<PokedexItem>>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<List<PokedexItem>>, t: Throwable) {
               Log.e(javaClass.name, t.message, t)
            }

        })
    }

    private fun populateItems(pokedexItems: List<PokedexItem>) {
        GlobalScope.launch {
            pokedexItems.forEach {
                var picturePath = downloadImageAndStore(
                    context,
                    it.url,
                    it.title.replace(" ", "_")
                )

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::explanation.name, it.explanation)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::date.name, it.date)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(POKEDEX_PROVIDER_URI, values)

            }
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<PokedexReceiver>()
        }
    }


}