package hr.algebra.pokedex.dao

import android.content.Context

fun getNasaRepository(context: Context?) = PokedexSqlHelper(context)