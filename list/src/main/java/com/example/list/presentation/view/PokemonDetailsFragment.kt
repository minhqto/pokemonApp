package com.example.list.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.list.databinding.FragmentPokemonDetailBinding
import com.example.list.presentation.viewmodel.PokemonDetailsViewModel

class PokemonDetailsFragment : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel

    private lateinit var binding: FragmentPokemonDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(this).get(PokemonDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}