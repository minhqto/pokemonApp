package com.example.list.presentation.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.list.R
import com.example.list.databinding.ActivityPokemonDetailBinding

class PokemonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager
            .setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to intent.getStringExtra(POKEMON_DETAILS)))

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(androidx.fragment.R.animator.fragment_fade_enter, androidx.fragment.R.animator.fragment_fade_exit)
            .add(R.id.fragment_container, PokemonDetailsFragment())
            .commit()
    }

    companion object {
        const val REQUEST_KEY = "pokemonRequest"
        const val BUNDLE_KEY = "pokemonName"
        const val POKEMON_DETAILS = "pokemonDetails"
    }
}