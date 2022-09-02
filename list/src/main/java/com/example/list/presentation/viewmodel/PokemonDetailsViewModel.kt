package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.list.domain.GetPokemonUseCase
import com.example.list.domain.GetPokemonsUseCase
import com.example.list.presentation.viewdata.PokemonDetailViewState
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PokemonDetailsViewModel : ViewModel() {
    private val getPokemonUseCase = GetPokemonUseCase()

    private val compositeDisposable = CompositeDisposable()

    private val _state = MutableStateFlow(PokemonDetailViewState(""))

    val state: StateFlow<PokemonDetailViewState> = _state

    fun getPokemonDetails(pokemonName: String) {
        getPokemonUseCase
            .execute(pokemonName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { it }
            .subscribe({
                   Log.v("Minh", it.weight.toString())
//                _state.value = PokemonDetailViewState(name = it?)
            }, {})
            .let {
                compositeDisposable.add(it)
            }
    }
}