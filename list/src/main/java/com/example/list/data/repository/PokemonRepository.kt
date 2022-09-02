package com.example.list.data.repository

import android.util.Log
import com.example.list.data.api.ApiBuilder
import com.example.list.data.api.PokemonApi

class PokemonRepository {
    private val pokemonApi: PokemonApi = ApiBuilder.api

    fun getPokemons() = pokemonApi.getPokemons(limit = 50)
        .map { it.body() }
        .doOnError {
            Log.e("Error", it.localizedMessage)
        }

    fun getPokemon(name: String) = pokemonApi.getPokemon(name)
        .map { it.body() }
        .doOnError {
            Log.e("Error", it.localizedMessage)
        }
}