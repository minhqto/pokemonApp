package com.example.list.presentation.viewdata

data class PokemonDetailViewState(
    val name: String? = null,
    val baseExperience: String? = null,
    val weight: Long? = null,
    val abilities: List<Ability> = emptyList()
)

data class Ability(
    val skillName: String,
    val isHidden: Boolean,
    val slot: Long,
)