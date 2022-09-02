package com.example.list.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.list.domain.GetPokemonsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonDetailsViewModel : ViewModel() {
    private val getPokemonsUseCase = GetPokemonsUseCase()

    private val compositeDisposable = CompositeDisposable()

    fun getPokemonDetails() {
        getPokemonsUseCase
            .execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.v("Minh", "Getting details!")
            }, {})
            .let {
                compositeDisposable.add(it)
            }
    }
}