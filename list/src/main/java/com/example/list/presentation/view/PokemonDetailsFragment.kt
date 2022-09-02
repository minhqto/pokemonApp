package com.example.list.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.list.databinding.FragmentPokemonDetailBinding
import com.example.list.presentation.viewdata.PokemonDetailViewState
import com.example.list.presentation.viewmodel.PokemonDetailsViewModel

class PokemonDetailsFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel

    private lateinit var binding: FragmentPokemonDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PokemonDetailsViewModel::class.java)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            bundle.getString("bundleKey")?.let {
                viewModel.getPokemonDetails(pokemonName = it)
            }
        }

        // first time using coroutines to subscribe to viewState
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                setViewContent(it)
            }
        }
    }

    private fun setViewContent(pokemonDetailViewState: PokemonDetailViewState) {
        binding.composeView.setContent {
            MainSurface(pokemonDetailViewState)
        }
    }
}

@Composable
fun MainSurface(pokemonDetailViewState: PokemonDetailViewState) {
    Column(
        modifier = Modifier
    ) {
        pokemonDetailViewState.name?.let {
            DataRow(label = "Pokemon name", it)
        }

        pokemonDetailViewState.baseExperience?.let {
            DataRow(label = "Base experience", it)
        }

        pokemonDetailViewState.weight?.let {
            DataRow(label = "Weight", it.toString())
        }

        if (pokemonDetailViewState.abilities.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Abilities: "
            )
            pokemonDetailViewState.abilities.forEach {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = it.skillName
                )
            }
        }
    }
}

@Composable
fun DataRow(label: String, data: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("$label: $data")
    }
}