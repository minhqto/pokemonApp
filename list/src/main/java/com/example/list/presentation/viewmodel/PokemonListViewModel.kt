package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list.data.model.PokemonResponseDTO
import com.example.list.domain.GetPokemonsUseCase
import com.example.list.presentation.view.PageDetails
import com.example.list.presentation.view.PageType
import com.example.list.presentation.viewdata.PokemonViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonListViewModel : ViewModel() {

    private val getPokemonsUseCase = GetPokemonsUseCase()

    val state: MutableLiveData<List<PokemonViewState>> = MutableLiveData()

    val openPageEvent: MutableLiveData<PageDetails> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun getPokemon() {
        getPokemonsUseCase
            .execute()
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

    fun handleClick(name: String) = openPageEvent.postValue(
        PageDetails(
            name = name,
            pageType = PageType.POKEMON_DETAIL_PAGE
        )
    )

    private fun mapToPresentation(pokemonDTO: PokemonResponseDTO) =
        pokemonDTO.results.map {
            PokemonViewState(
                name = it.name,
                url = it.url
            )
        }
}