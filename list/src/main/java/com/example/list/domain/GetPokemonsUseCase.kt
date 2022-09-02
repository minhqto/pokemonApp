package com.example.list.domain

import com.example.list.data.repository.PokemonRepository

class GetPokemonsUseCase {
    private val pokemonRepository = PokemonRepository()

    fun execute() = pokemonRepository.getPokemons()
}