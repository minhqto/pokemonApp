package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list.data.model.PokemonsResponseDTO
import com.example.list.domain.GetPokemonsUseCase
import com.example.list.presentation.view.PageDetails
import com.example.list.presentation.view.PageType
import com.example.list.presentation.viewdata.PokemonCellViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PokemonListViewModel : ViewModel() {

    private val getPokemonsUseCase = GetPokemonsUseCase()

    val openPageEvent: MutableLiveData<PageDetails> = MutableLiveData()

    val viewState: Flowable<ViewState>

    private val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    init {
        val results: Flowable<ViewResult> = intentToResult(
            intentSubject.toFlowable(BackpressureStrategy.LATEST),
            getPokemonsUseCase
        ).share()

        viewState = results.resultToViewState(
            pokemonPresentationMapper = PokemonPresentationMapper()
        )
    }

    sealed class ViewIntent {
        object LoadPokemon : ViewIntent()
    }

    sealed class ViewResult {
        class PokemonLoaded(val pokemonsResponse: PokemonsResponseDTO) : ViewResult()
    }

    data class ViewState(
        val pokemons: List<PokemonCellViewState> = emptyList(),
        val isLoading: Boolean = false
    )

    fun process(intent: ViewIntent) {
        intentSubject.onNext(intent)
    }

    fun handleClick(name: String) = openPageEvent.postValue(
        PageDetails(
            name = name,
            pageType = PageType.POKEMON_DETAIL_PAGE
        )
    )
}

private fun intentToResult(
    intents: Flowable<PokemonListViewModel.ViewIntent>,
    getPokemonsUseCase: GetPokemonsUseCase
) : Flowable<PokemonListViewModel.ViewResult> = intents.flatMap { intent ->
    when (intent) {
        is PokemonListViewModel.ViewIntent.LoadPokemon -> {
            getPokemonsUseCase
                .execute()
                .toFlowable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    PokemonListViewModel.ViewResult.PokemonLoaded(
                        pokemonsResponse = it
                    )
                }
                .doOnError {
                    Log.v("ERROR", it.localizedMessage)
                }
        }
    }
}

private fun Flowable<PokemonListViewModel.ViewResult>.resultToViewState(
    pokemonPresentationMapper: PokemonPresentationMapper
) : Flowable<PokemonListViewModel.ViewState> = scan(
    PokemonListViewModel.ViewState()
) { prevState, viewResult ->
    when (viewResult) {
        is PokemonListViewModel.ViewResult.PokemonLoaded -> {
            prevState.copy(
                pokemons = pokemonPresentationMapper.mapToPresentation(viewResult.pokemonsResponse)
            )
        }
    }
}
