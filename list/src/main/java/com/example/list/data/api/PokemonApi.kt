package com.example.list.data.api

import com.example.list.data.model.PokemonResponseDTO
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    fun getPokemon(@Query("limit") limit: Long? = null): Single<Response<PokemonResponseDTO>>
}