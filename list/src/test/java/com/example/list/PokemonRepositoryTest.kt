package com.example.list

import com.example.list.data.api.PokemonApi
import com.example.list.data.model.PokemonResultDTO
import com.example.list.data.model.PokemonsResponseDTO
import com.example.list.data.repository.PokemonRepository
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class PokemonRepositoryTest {

    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var api: PokemonApi

    @Before
    fun setup() {
        api = mock()
        pokemonRepository = PokemonRepository(api)
    }

    @Test
    fun `repo returns single mapped pokemon when api returns pokemonResponse`() {
        // given
        whenever(api.getPokemons(any()))
            .thenReturn(
                Single.just(
                    Response.success(
                        PokemonsResponseDTO(
                            count = 1,
                            next = "",
                            previous = "",
                            results = listOf(
                                PokemonResultDTO(
                                    name = "Charizard",
                                    url = "https://test.com"
                                )
                            )
                        )
                    )
                )
            )
        // when
        val result = pokemonRepository.getPokemons().blockingGet()
        // then
        assert(result.isNotEmpty())
        assert(result.size == 1)
        assert(result[0].name == "Charizard")
    }

    @Test
    fun `repo returns error when api throws an error`() {
        // given
        whenever(api.getPokemons(any()))
            .thenReturn(
                Single.just(
                    Response.error(
                        400, ResponseBody.create(
                            null,
                            "Error!!"
                        )
                    )
                )
            )
        // when
        val result = pokemonRepository.getPokemons().blockingGet()

        // then
        assert(result.isEmpty())
    }
}