package com.example.list.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.list.presentation.viewdata.PokemonCellViewState

@Composable
fun PokemonRowCell(
    pokemonCellViewState: PokemonCellViewState,
    modifier: Modifier = Modifier,
    onClick: (name: String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(Color.White)
            .clickable {
                onClick(pokemonCellViewState.name)
            }

    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = pokemonCellViewState.name
        )
    }
}