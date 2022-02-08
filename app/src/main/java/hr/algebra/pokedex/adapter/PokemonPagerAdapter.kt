package hr.algebra.pokedex.adapter

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
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
import hr.algebra.pokedex.model.Pokemon
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File




class PokemonPagerAdapter(private val context: Context, private val pokemon: MutableList<Pokemon>)
    : RecyclerView.Adapter<PokemonPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivPokemon = itemView.findViewById<ImageView>(R.id.ivPokemon)
        val ivCaught: ImageView = itemView.findViewById<ImageView>(R.id.ivCaught)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvWeight = itemView.findViewById<TextView>(R.id.tvWeight)
        private val tvHeight = itemView.findViewById<TextView>(R.id.tvHeight)
        private val lbHeight = itemView.findViewById<TextView>(R.id.lbHeight)
        private val tvAbilities = itemView.findViewById<TextView>(R.id.tvAbilities)
        private val tvTypes = itemView.findViewById<TextView>(R.id.tvType)

        @SuppressLint("SetTextI18n")
        fun bind(pokemon : Pokemon) {
            Picasso.get()
                .load(File(pokemon.spritePath))
                .error(R.drawable.poke_logo)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivPokemon)
            tvName.text = pokemon.name
            tvWeight.text="${pokemon.weight} kg"
            tvHeight.text="${pokemon.height} dm"
            tvAbilities.text = pokemon.abilities
            tvTypes.text = pokemon.types
            ivCaught.setImageResource(if (pokemon.caught) R.drawable.caught_green else R.drawable.caught_red)
            lbHeight.text= "Height"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(itemView = LayoutInflater
        .from(parent.context).inflate(R.layout.item_pager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemon[position]
        holder.ivCaught.setOnClickListener {
            pokemon.caught = !pokemon.caught
            val uri = ContentUris.withAppendedId(POKEDEX_PROVIDER_URI, pokemon._id!!)
            val values = ContentValues().apply {
                put(Pokemon::caught.name, pokemon.caught)
            }
            context.contentResolver.update(
                uri,
                values,
                null,
                null
            )
            notifyItemChanged(position)
        }
        holder.bind(pokemon)
        //Log.w("PokemonPagerAdapter","Pager bonded")
    }

    override fun getItemCount() = pokemon.size
}