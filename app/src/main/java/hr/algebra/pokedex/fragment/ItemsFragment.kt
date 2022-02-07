package hr.algebra.pokedex.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.pokedex.adapter.PokemonAdapter
import hr.algebra.pokedex.databinding.FragmentItemsBinding
import hr.algebra.pokedex.framework.fetchPokedex
import hr.algebra.pokedex.model.Pokemon

class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding
    private lateinit var pokemons: MutableList<Pokemon>
    //private lateinit var items: MutableList<Item>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        //items = requireContext().fetchItems()
        pokemons=requireContext().fetchPokedex()
        Log.d("Pokemons","${pokemons.toString()}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PokemonAdapter(context, pokemons)
        }
        Log.w("ItemsFragment","created")
    }

}