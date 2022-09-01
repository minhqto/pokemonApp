package com.example.list.data

import android.util.Log

class PokemonRepository {
    private val pokemonApi: PokemonApi = ApiBuilder.api

    fun getPokemon() = pokemonApi.getPokemon(limit = 50)
        .map { it.body() }
        .doOnError {
            Log.e("Error", it.localizedMessage)
        }
}