package com.example.list.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.list.R
import com.example.list.databinding.ActivityListBinding
import com.example.list.presentation.view.PokemonDetailsActivity.Companion.POKEMON_DETAILS
import com.example.list.presentation.viewmodel.PokemonListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var adapter: PokemonListAdapter

    private lateinit var pokemonListViewModel: PokemonListViewModel

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)

        pokemonListViewModel = PokemonListViewModel()

        setContentView(binding.root)
        adapter = PokemonListAdapter(viewModel = pokemonListViewModel)

        subscribeViewState()
        subscribeViewEffects()
        setupRecyclerView()

        pokemonListViewModel.process(PokemonListViewModel.ViewIntent.LoadPokemon)
    }

    private fun subscribeViewState() {
        pokemonListViewModel
            .viewState
            .subscribe {
                adapter.data = it.pokemons
                binding.progressSpinner.isVisible = it.isLoading
            }
            .let {
                compositeDisposable.add(it)
            }
    }

    private fun subscribeViewEffects() {
        pokemonListViewModel
            .viewEffects
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { viewEffect ->
                when (viewEffect) {
                    is PokemonListViewModel.ViewEffect.ShowPokemonDetails -> {
                        val pokemonDetailIntent = Intent(this, PokemonDetailsActivity::class.java)
                        pokemonDetailIntent.putExtra(POKEMON_DETAILS, viewEffect.pokemonName)
                        startActivity(pokemonDetailIntent)
                    }
                    is PokemonListViewModel.ViewEffect.ShowErrorToast -> Toast.makeText(this, "Error loading pokemon!", Toast.LENGTH_SHORT).show()
                    else -> { }
                }
            }
            .let {
                compositeDisposable.add(it)
            }
    }

    private fun setupRecyclerView() = binding.pokemonList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PokemonListActivity.adapter
        }
}