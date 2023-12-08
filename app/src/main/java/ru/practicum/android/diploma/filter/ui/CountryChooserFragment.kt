package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryChooserBinding
import ru.practicum.android.diploma.filter.ui.adapters.CountryAdapter

class CountryChooserFragment : Fragment(R.layout.fragment_country_chooser) {

    private var _binding: FragmentCountryChooserBinding? = null
    private val binding get() = _binding!!

    private val adapter = CountryAdapter {

    }

//    private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountryChooserBinding.bind(view)

        binding.rvCountry.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCountry.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
