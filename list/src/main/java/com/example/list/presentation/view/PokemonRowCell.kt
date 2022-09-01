package com.example.list.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.list.presentation.viewdata.PokemonViewState

@Composable
fun PokemonRowCell(
    pokemonViewState: PokemonViewState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
            .background(Color.White)

    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = pokemonViewState.name
        )
    }
}