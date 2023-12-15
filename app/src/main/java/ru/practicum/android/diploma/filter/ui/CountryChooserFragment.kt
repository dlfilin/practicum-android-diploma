package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryChooserBinding
import ru.practicum.android.diploma.filter.presentation.CountryViewModel
import ru.practicum.android.diploma.filter.ui.adapters.CountryAdapter

class CountryChooserFragment : Fragment(R.layout.fragment_country_chooser) {

    private var _binding: FragmentCountryChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountryViewModel by viewModel()

    private val adapter = CountryAdapter {
        // записываем выбор в шаред префс и идем назад
        viewModel.saveCountry(it)
        findNavController().navigateUp()
    }

    //  private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCountryChooserBinding.bind(view)

        binding.rvCountry.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvCountry.adapter = adapter

        lifecycleScope.launch {
            viewModel.getCountries().collect {
                adapter.updateData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
