package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryChooserBinding
import ru.practicum.android.diploma.filter.presentation.CountryChooserScreenState
import ru.practicum.android.diploma.filter.presentation.CountryViewModel
import ru.practicum.android.diploma.filter.ui.adapters.CountryAdapter

class CountryChooserFragment : Fragment(R.layout.fragment_country_chooser) {

    private var _binding: FragmentCountryChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountryViewModel by viewModel()

    private val adapter = CountryAdapter {
        viewModel.saveFilterToPrefs(it)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountryChooserBinding.bind(view)

        binding.rvCountry.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCountry.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: CountryChooserScreenState) {
        when (state) {
            is CountryChooserScreenState.Content -> {
                adapter.updateData(state.items)
                binding.rvCountry.isVisible = true
                binding.placeholderImage.isVisible = false
                binding.placeholderMessage.isVisible = false
            }

            is CountryChooserScreenState.Error -> {
                binding.rvCountry.isVisible = false
                binding.placeholderImage.isVisible = true
                binding.placeholderMessage.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
