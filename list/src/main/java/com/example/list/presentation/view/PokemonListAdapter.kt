package com.example.list.presentation.view

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.example.list.presentation.viewdata.PokemonViewState
import com.example.list.presentation.viewmodel.PokemonListViewModel

class PokemonListAdapter(
    private val viewModel: PokemonListViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<PokemonViewState> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        PokemonViewHolder(ComposeView(parent.context))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PokemonViewHolder).pokemonItemComposeView.setContent {
                PokemonRowCell(pokemonViewState = data[position], onClick = { viewModel.handleClick(it) })
            }
    }

    override fun getItemCount(): Int {
            return data.size
        }
}

class PokemonViewHolder(val pokemonItemComposeView: ComposeView) : RecyclerView.ViewHolder(pokemonItemComposeView)