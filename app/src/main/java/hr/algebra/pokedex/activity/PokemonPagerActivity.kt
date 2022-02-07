package hr.algebra.pokedex.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.pokedex.adapter.PokemonPagerAdapter
import hr.algebra.pokedex.databinding.ActivityPokemonPagerBinding
import hr.algebra.pokedex.framework.fetchPokedex
import hr.algebra.pokedex.model.Pokemon

const val POKEMON_POSITION = "hr.algebra.pokedex.pokemon_position"

class PokemonPagerActivity : AppCompatActivity() {

    private lateinit var pokemons: MutableList<Pokemon>
    private lateinit var binding: ActivityPokemonPagerBinding

    private var pokemonPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initPager() {
        pokemons = fetchPokedex()
        pokemonPosition = intent.getIntExtra(POKEMON_POSITION, 0)
        binding.viewPager.adapter = PokemonPagerAdapter(this, pokemons)
        binding.viewPager.currentItem = pokemonPosition
    }
}