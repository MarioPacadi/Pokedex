package hr.algebra.pokedex

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.pokedex.api.PokedexFetcher
import kotlinx.coroutines.DelicateCoroutinesApi

private const val JOB_ID = 1
@DelicateCoroutinesApi
class PokedexService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        PokedexFetcher(this).fetchItems()
    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, PokedexService::class.java, JOB_ID, intent)
        }
    }
}