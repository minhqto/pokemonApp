package com.example.list.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.list.R
import com.example.list.databinding.ActivityListBinding
import com.example.list.presentation.viewmodel.PokemonDetailsViewModel
import com.example.list.presentation.viewmodel.PokemonListViewModel

class PokemonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var adapter: PokemonListAdapter

    private lateinit var viewModel: PokemonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)

        viewModel = PokemonListViewModel()
        viewModel.getPokemon()

        setContentView(binding.root)
        adapter = PokemonListAdapter(viewModel = viewModel)

        subscribeViewState()
        setupRecyclerView()
    }

    private fun subscribeViewState() {
        viewModel
            .state
            .observe(this) { pokemonViewState ->
                adapter.data = pokemonViewState
            }

        viewModel
            .openPageEvent
            .observe(this) { pageDetails ->
                when (pageDetails.pageType) {
                    PageType.POKEMON_DETAIL_PAGE -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(androidx.fragment.R.animator.fragment_fade_enter, androidx.fragment.R.animator.fragment_fade_exit)
                            .add(R.id.fragment_container, PokemonDetailsFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }

            }
    }
    private fun setupRecyclerView() = binding.pokemonList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PokemonListActivity.adapter
        }
}