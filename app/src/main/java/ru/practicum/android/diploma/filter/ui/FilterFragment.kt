package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

//    private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFilterBinding.bind(view)

        binding.gotoWorkPlaceFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_workPlaceFragment)
        }
        binding.gotoIndustryFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryChooserFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
