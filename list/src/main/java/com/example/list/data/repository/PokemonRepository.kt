package com.example.list.data.repository

import android.util.Log
import com.example.list.data.api.ApiBuilder
import com.example.list.data.api.PokemonApi
import com.example.list.data.model.PokemonsResponseDTO
import com.example.list.domain.model.Pokemon

class PokemonRepository(
    private val pokemonApi: PokemonApi = ApiBuilder.api
) {

    fun getPokemons() = pokemonApi.getPokemons(limit = 50)
        .map {
            if (it.isSuccessful) {
                it.body()?.let { pokemonResponseDTO ->
                    mapToDomain(pokemonResponseDTO)
                } ?: emptyList()
            } else {
                emptyList()
            }
        }

    suspend fun getPokemon(name: String) = pokemonApi.getPokemon(name)
}

private fun mapToDomain(pokemonsResponseDTO: PokemonsResponseDTO): List<Pokemon> =
    pokemonsResponseDTO.results.map {
        Pokemon(it.name)
    }