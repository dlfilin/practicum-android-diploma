package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionChooserBinding

class RegionChooserFragment : Fragment(R.layout.fragment_region_chooser) {

    private var _binding: FragmentRegionChooserBinding? = null
    private val binding get() = _binding!!

//    private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegionChooserBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
