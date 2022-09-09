package com.example.list.domain.usecase

import com.example.list.data.repository.PokemonRepository

class GetPokemonUseCase {
    private val pokemonRepository = PokemonRepository()

    suspend fun execute(name: String) = pokemonRepository.getPokemon(name).body()
}