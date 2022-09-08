package com.example.list.presentation.viewmodel

import com.example.list.data.model.PokemonsResponseDTO
import com.example.list.presentation.viewdata.PokemonCellViewState

class PokemonPresentationMapper {

    fun mapToPresentation(pokemonDTO: PokemonsResponseDTO) =
        pokemonDTO.results.map {
            PokemonCellViewState(
                name = it.name,
                url = it.url
            )
        }
}