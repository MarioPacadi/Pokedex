package hr.algebra.pokedex

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.pokedex.api.PokedexFetcher

private const val JOB_ID = 1
class NasaService : JobIntentService() {
    // bg metoda
    override fun onHandleWork(intent: Intent) {

        PokedexFetcher(this).fetchItems()


    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, NasaService::class.java, JOB_ID, intent)
        }
    }
}