package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list.data.model.PokemonsResponseDTO
import com.example.list.domain.GetPokemonsUseCase
import com.example.list.presentation.view.PageDetails
import com.example.list.presentation.view.PageType
import com.example.list.presentation.viewdata.PokemonCellViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonListViewModel : ViewModel() {

    private val getPokemonsUseCase = GetPokemonsUseCase()

    val state: MutableLiveData<List<PokemonCellViewState>> = MutableLiveData()

    val openPageEvent: MutableLiveData<PageDetails> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    fun getPokemons() {
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

    private fun mapToPresentation(pokemonDTO: PokemonsResponseDTO) =
        pokemonDTO.results.map {
            PokemonCellViewState(
                name = it.name,
                url = it.url
            )
        }
}