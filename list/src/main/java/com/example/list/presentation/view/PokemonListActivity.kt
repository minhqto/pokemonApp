package com.example.list.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.list.R
import com.example.list.databinding.ActivityListBinding
import com.example.list.presentation.viewmodel.PokemonListViewModel

class PokemonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private lateinit var adapter: PokemonListAdapter

    private lateinit var pokemonListViewModel: PokemonListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)

        pokemonListViewModel = PokemonListViewModel()
        pokemonListViewModel.getPokemons()

        setContentView(binding.root)
        adapter = PokemonListAdapter(viewModel = pokemonListViewModel)

        subscribeViewState()
        setupRecyclerView()
    }

    private fun subscribeViewState() {
        pokemonListViewModel
            .state
            .observe(this) { pokemonViewState ->
                adapter.data = pokemonViewState
            }

        pokemonListViewModel
            .openPageEvent
            .observe(this) { pageDetails ->
                when (pageDetails.pageType) {
                    PageType.POKEMON_DETAIL_PAGE -> {
                        supportFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to pageDetails.name))
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