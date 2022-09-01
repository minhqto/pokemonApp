package com.example.list.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponseDTO(
    @SerializedName("count") val count: Long,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: List<PokemonResultDTO>
)

data class PokemonResultDTO(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)