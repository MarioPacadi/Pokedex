package hr.algebra.pokedex.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.pokedex.R
import hr.algebra.pokedex.model.Pokemon
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class PokemonPagerAdapter(private val context: Context, private val pokemon: MutableList<Pokemon>)
    : RecyclerView.Adapter<PokemonPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvExplanation = itemView.findViewById<TextView>(R.id.tvExplanation)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        fun bind(pokemon : Pokemon) {
            Picasso.get()
                .load(File(pokemon.spritePath))
                .error(R.drawable.poke_logo)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvTitle.text = pokemon.name
            tvExplanation.text = pokemon.abilities.toString()
            tvDate.text = pokemon.types.toString()
            //ivRead.setImageResource(if (item.read) R.drawable.green_flag else R.drawable.red_flag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(itemView = LayoutInflater
        .from(parent.context).inflate(R.layout.item_pager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemon[position]
//        holder.ivRead.setOnClickListener {
//            item.read = !item.read
//            val uri = ContentUris.withAppendedId(POKEDEX_PROVIDER_URI, item._id!!)
//            val values = ContentValues().apply {
//                put(Item::read.name, item.read)
//            }
//            context.contentResolver.update(
//                uri,
//                values,
//                null,
//                null
//            )
//            notifyItemChanged(position)
//        }
        holder.bind(pokemon)
        Log.w("PokemonPagerAdapter","Pager bonded")
    }

    override fun getItemCount() = pokemon.size
}