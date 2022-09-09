package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.list.data.model.SinglePokemonResponseDTO
import com.example.list.domain.usecase.GetPokemonUseCase
import com.example.list.presentation.viewdata.Ability
import com.example.list.presentation.viewdata.PokemonDetailViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PokemonDetailsViewModel : ViewModel() {
    private val getPokemonUseCase = GetPokemonUseCase()

    private val _state = MutableStateFlow(PokemonDetailViewState())

    val state: StateFlow<PokemonDetailViewState> = _state

    fun getPokemonDetails(pokemonName: String) {
        CoroutineScope(IO + CoroutineExceptionHandler { _, throwable ->
            Log.v("error", throwable.localizedMessage)
        }).launch {
            getPokemonUseCase
                .execute(pokemonName)?.let {
                    _state.value = mapToPresentation(it)
                }
        }
    }

    private fun mapToPresentation(singlePokemonResponseDTO: SinglePokemonResponseDTO) =
        PokemonDetailViewState(
            name = singlePokemonResponseDTO.name,
            baseExperience = singlePokemonResponseDTO.baseExperience,
            weight = singlePokemonResponseDTO.weight,
            abilities = singlePokemonResponseDTO.abilities.map { abilityDTO ->
                Ability(
                    skillName = abilityDTO.ability.name,
                    isHidden = abilityDTO.isHidden,
                    slot = abilityDTO.slot
                )
            }
        )
}