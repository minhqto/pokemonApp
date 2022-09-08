package com.example.list.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.list.R
import com.example.list.databinding.ActivityListBinding
import com.example.list.presentation.view.PokemonDetailsActivity.Companion.POKEMON_DETAILS
import com.example.list.presentation.viewmodel.PokemonListViewModel
import io.reactivex.disposables.CompositeDisposable

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
        setupRecyclerView()

        pokemonListViewModel.process(PokemonListViewModel.ViewIntent.LoadPokemon)
    }

    private fun subscribeViewState() {
        pokemonListViewModel
            .viewState
            .subscribe {
                adapter.data = it.pokemons
            }
            .let {
                compositeDisposable.add(it)
            }

        pokemonListViewModel
            .openPageEvent
            .observe(this) { pageDetails ->
                when (pageDetails.pageType) {
                    PageType.POKEMON_DETAIL_PAGE -> {
                        val pokemonDetailIntent = Intent(this, PokemonDetailsActivity::class.java)
                        pokemonDetailIntent.putExtra(POKEMON_DETAILS, pageDetails.name)
                        startActivity(pokemonDetailIntent)
                    }
                }
            }
    }
    private fun setupRecyclerView() = binding.pokemonList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PokemonListActivity.adapter
        }
}