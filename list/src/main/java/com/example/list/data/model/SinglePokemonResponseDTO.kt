package com.example.list.data.model

import com.google.gson.annotations.SerializedName

data class SinglePokemonResponseDTO(
    @SerializedName("base_experience") val baseExperience: String,
    @SerializedName("weight") val weight: Long,
    @SerializedName("abilities") val abilities: List<PokemonAbilitiesDTO>
)

data class PokemonAbilitiesDTO(
    @SerializedName("ability") val ability: AbilityDTO,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Long
)

data class AbilityDTO(
    @SerializedName("name") val name: String
)