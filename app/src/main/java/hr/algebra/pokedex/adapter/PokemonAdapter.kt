package hr.algebra.pokedex.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.pokedex.POKEDEX_PROVIDER_URI
import hr.algebra.pokedex.R
import hr.algebra.pokedex.activity.POKEMON_POSITION
import hr.algebra.pokedex.activity.PokemonPagerActivity
import hr.algebra.pokedex.framework.startActivity
import hr.algebra.pokedex.model.Pokemon
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class PokemonAdapter(private val context: Context, private val pokemons: MutableList<Pokemon>)
    : RecyclerView.Adapter<PokemonAdapter.ViewHolder>(){
    class ViewHolder(pokemonView: View) : RecyclerView.ViewHolder(pokemonView){

        private val ivItem = pokemonView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = pokemonView.findViewById<TextView>(R.id.tvItem)
        fun bind(pokemon: Pokemon) {
            Picasso.get()
                .load(File(pokemon.spritePath))
                .error(R.drawable.poke_logo)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvItem.text = pokemon.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(pokemonView = LayoutInflater
        .from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity<PokemonPagerActivity>(POKEMON_POSITION, position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.sure) + " '${pokemons[position].name}'?")
                setIcon(R.drawable.delete)
                setCancelable(true)
                setNegativeButton(R.string.cancel, null)
                setPositiveButton("OK") { _, _ -> deleteItem(position)}
                show()
            }
            true
        }
        holder.bind(pokemons[position])
        Log.w("PokemonAdapter","Adapter bonded")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteItem(position: Int) {
        val pokemon = pokemons[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(POKEDEX_PROVIDER_URI, pokemon._id!!),
            null,
            null)
        File(pokemon.spritePath).delete()
        pokemons.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = pokemons.size
}