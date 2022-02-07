package hr.algebra.pokedex.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.model.Item
import hr.algebra.pokedex.model.Pokemon

fun View.startAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun<reified T : Activity> Context.startActivity()
    = startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })

inline fun<reified T : Activity> Context.startActivity(key: String, value: Int)
    = startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    })
inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
    = sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()


fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)


fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, function: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        function,
        delay
    )
}

fun Context.fetchItems() : MutableList<Item> {
    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query(POKEDEX_PROVIDER_URI,
    null,
    null,
    null,
    null)
    while (cursor != null && cursor.moveToNext()) {
        items.add(Item(
            cursor.getLong(cursor.getColumnIndexOrThrow(Item::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::title.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::explanation.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::picturePath.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::date.name)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Item::read.name)) == 1
        ))
    }
    return items
}

fun Context.fetchPokedex() : MutableList<Pokemon> {
    val pokemons = mutableListOf<Pokemon>()
    val cursor = contentResolver?.query(POKEDEX_PROVIDER_URI,
        null,
        null,
        null,
        null)
    while (cursor != null && cursor.moveToNext()) {
        pokemons.add(
            Pokemon(
            cursor.getLong(cursor.getColumnIndexOrThrow(Pokemon::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Pokemon::name.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(Pokemon::weight.name)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(Pokemon::height.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Pokemon::spritePath.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Pokemon::types.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Pokemon::abilities.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Pokemon::moves.name)),
            )
        )
    }
    return pokemons
}
