package com.example.list.domain

import com.example.list.data.repository.PokemonRepository

class GetPokemonUseCase {
    private val pokemonRepository = PokemonRepository()

    fun getPokemon() = pokemonRepository.getPokemon()
}