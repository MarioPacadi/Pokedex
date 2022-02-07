package hr.algebra.pokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import hr.algebra.pokedex.activity.HostActivity
import hr.algebra.pokedex.framework.startActivity

class PokedexReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.w("PokedexFetcher","Fetchers complete")
        context.startActivity<HostActivity>()
    }
}