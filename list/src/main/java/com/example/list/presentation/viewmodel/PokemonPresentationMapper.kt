package com.example.list.presentation.viewmodel

import com.example.list.domain.model.Pokemon
import com.example.list.presentation.viewdata.PokemonCellViewState

class PokemonPresentationMapper {

    fun mapToPresentation(pokemons: List<Pokemon>) =
        pokemons.map {
            PokemonCellViewState(
                name = it.name
            )
        }
}