package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.list.domain.GetPokemonsUseCase
import com.example.list.domain.model.Pokemon
import com.example.list.presentation.viewdata.PokemonCellViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PokemonListViewModel : ViewModel() {

    private val getPokemonsUseCase = GetPokemonsUseCase()

    val viewState: Flowable<ViewState>
    val viewEffects: Flowable<ViewEffect>

    private val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    init {
        val results: Flowable<ViewResult> = intentToResult(
            intentSubject.toFlowable(BackpressureStrategy.LATEST),
            getPokemonsUseCase
        ).share()

        viewState = results.resultToViewState(
            pokemonPresentationMapper = PokemonPresentationMapper()
        )

        viewEffects = results.resultToViewEffect()
    }

    sealed class ViewIntent {
        object LoadPokemon : ViewIntent()
        class OpenPokemonDetails(val pokemonName: String) : ViewIntent()
    }

    sealed class ViewResult {
        class PokemonLoaded(val pokemons: List<Pokemon>) : ViewResult()
        class PokemonDetailsOpened(val pokemonDetails: String) : ViewResult()
    }

    data class ViewState(
        val pokemons: List<PokemonCellViewState> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class ViewEffect {
        class ShowPokemonDetails(val pokemonName: String) : ViewEffect()
        object None : ViewEffect()
    }

    fun process(intent: ViewIntent) {
        intentSubject.onNext(intent)
    }
}

private fun intentToResult(
    intents: Flowable<PokemonListViewModel.ViewIntent>,
    getPokemonsUseCase: GetPokemonsUseCase
): Flowable<PokemonListViewModel.ViewResult> = intents.flatMap { intent ->
    when (intent) {
        is PokemonListViewModel.ViewIntent.LoadPokemon -> {
            getPokemonsUseCase
                .execute()
                .toFlowable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map { pokemonList ->
                    PokemonListViewModel.ViewResult.PokemonLoaded(
                        pokemons = pokemonList
                    )
                }
                .doOnError {
                    Log.v("ERROR", it.localizedMessage)
                }
        }
        is PokemonListViewModel.ViewIntent.OpenPokemonDetails -> {
            Flowable.just(PokemonListViewModel.ViewResult.PokemonDetailsOpened(
                pokemonDetails = intent.pokemonName
            ))
        }
    }
}

private fun Flowable<PokemonListViewModel.ViewResult>.resultToViewState(
    pokemonPresentationMapper: PokemonPresentationMapper
): Flowable<PokemonListViewModel.ViewState> = scan(
    PokemonListViewModel.ViewState()
) { prevState, viewResult ->
    when (viewResult) {
        is PokemonListViewModel.ViewResult.PokemonLoaded -> {
            prevState.copy(pokemons = pokemonPresentationMapper.mapToPresentation(viewResult.pokemons))
        }
        else -> prevState
    }
}

private fun Flowable<PokemonListViewModel.ViewResult>.resultToViewEffect(): Flowable<PokemonListViewModel.ViewEffect> =
    map { viewResult ->
        when (viewResult) {
            is PokemonListViewModel.ViewResult.PokemonDetailsOpened -> PokemonListViewModel.ViewEffect.ShowPokemonDetails(viewResult.pokemonDetails)
            else -> PokemonListViewModel.ViewEffect.None
        }
    }
