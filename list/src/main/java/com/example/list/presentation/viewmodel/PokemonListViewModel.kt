package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list.data.PokemonResponseDTO
import com.example.list.domain.GetPokemonUseCase
import com.example.list.presentation.viewdata.PokemonViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonListViewModel : ViewModel() {

    private val getPokemonUseCase = GetPokemonUseCase()

    val state: MutableLiveData<List<PokemonViewState>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun getPokemon() {
        getPokemonUseCase
            .getPokemon()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { responseDTO ->
                mapToPresentation(responseDTO)
            }
            .subscribe({
                state.postValue(it)
            }, {
                Log.v("Minh error", it.localizedMessage)
            })
            .let {
                compositeDisposable.add(it)
            }
    }

    private fun mapToPresentation(pokemonDTO: PokemonResponseDTO) =
        pokemonDTO.results.map {
            PokemonViewState(
                name = it.name,
                url = it.url
            )
        }
}