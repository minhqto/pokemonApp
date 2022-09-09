package com.example.list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.list.domain.usecase.GetPokemonsUseCase
import com.example.list.domain.model.Pokemon
import com.example.list.presentation.viewdata.PokemonCellViewState
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PokemonListViewModel : ViewModel() {

    private val getPokemonsUseCase = GetPokemonsUseCase()

    private val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    val viewState: Flowable<ViewState>
    val viewEffects: Flowable<ViewEffect>

    init {
        val results: Flowable<ViewResult> = intentToResult(
            intentSubject.toFlowable(BackpressureStrategy.LATEST),
            getPokemonsUseCase
        )

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
        object PokemonLoading : ViewResult()
        object LoadingError : ViewResult()
    }

    data class ViewState(
        val pokemons: List<PokemonCellViewState> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class ViewEffect {
        class ShowPokemonDetails(val pokemonName: String) : ViewEffect()
        object ShowErrorToast : ViewEffect()
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
                        pokemons = pokemonList.sortedBy {
                            it.name
                        }
                    ) as PokemonListViewModel.ViewResult
                }
                .onErrorReturn {
                    PokemonListViewModel.ViewResult.LoadingError
                }
                .startWith(PokemonListViewModel.ViewResult.PokemonLoading)
        }
        is PokemonListViewModel.ViewIntent.OpenPokemonDetails -> {
            Flowable.just(
                PokemonListViewModel.ViewResult.PokemonDetailsOpened(
                    pokemonDetails = intent.pokemonName
                )
            )
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
            prevState.copy(
                pokemons = pokemonPresentationMapper.mapToPresentation(viewResult.pokemons),
                isLoading = false
            )
        }
        is PokemonListViewModel.ViewResult.PokemonLoading -> prevState.copy(isLoading = true)
        is PokemonListViewModel.ViewResult.LoadingError -> prevState.copy(isLoading = false)
        else -> prevState
    }
}

private fun Flowable<PokemonListViewModel.ViewResult>.resultToViewEffect(): Flowable<PokemonListViewModel.ViewEffect> =
    map { viewResult ->
        when (viewResult) {
            is PokemonListViewModel.ViewResult.PokemonDetailsOpened -> PokemonListViewModel.ViewEffect.ShowPokemonDetails(
                viewResult.pokemonDetails
            )
            is PokemonListViewModel.ViewResult.LoadingError -> PokemonListViewModel.ViewEffect.ShowErrorToast
            else -> PokemonListViewModel.ViewEffect.None
        }
    }
