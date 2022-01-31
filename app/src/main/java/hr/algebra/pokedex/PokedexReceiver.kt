package hr.algebra.pokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.pokedex.framework.startActivity

class PokedexReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<hr.algebra.pokedex.HostActivity>()
    }
}