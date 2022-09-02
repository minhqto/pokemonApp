package com.example.list.data.api

import com.example.list.data.model.SinglePokemonResponseDTO
import com.example.list.data.model.PokemonsResponseDTO
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    fun getPokemons(@Query("limit") limit: Long? = null): Single<Response<PokemonsResponseDTO>>

    @GET("pokemon/{${POKEMON_NAME}}")
    fun getPokemon(@Path(POKEMON_NAME) name: String): Single<Response<SinglePokemonResponseDTO>>

   companion object {
        const val POKEMON_NAME = "name"
    }
}