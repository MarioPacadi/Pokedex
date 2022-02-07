package hr.algebra.pokedex.dao

import android.content.Context

fun getPokemonRepository(context: Context?) = PokedexSqlHelper(context)