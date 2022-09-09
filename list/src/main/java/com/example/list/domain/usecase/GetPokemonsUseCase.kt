package com.example.list.domain.usecase

import com.example.list.data.repository.PokemonRepository

class GetPokemonsUseCase {
    private val pokemonRepository = PokemonRepository()

    fun execute() = pokemonRepository.getPokemons()
}