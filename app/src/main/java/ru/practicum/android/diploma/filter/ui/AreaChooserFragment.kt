package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaChooserBinding
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.presentation.AreaChooserScreenState
import ru.practicum.android.diploma.filter.presentation.AreaViewModel
import ru.practicum.android.diploma.filter.ui.adapters.AreaAdapter

class AreaChooserFragment : Fragment(R.layout.fragment_area_chooser) {

    private var _binding: FragmentAreaChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AreaViewModel by viewModel()

    private val adapter = AreaAdapter {
        viewModel.saveFilterToPrefs(it)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAreaChooserBinding.bind(view)

        setRv()
        setListeners()
        setObservables()
    }

    private fun setRv() {
        binding.rvArea.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvArea.adapter = adapter
    }

    private fun setListeners() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.searchLayoutField.apply {
                    setEndIconDrawable(R.drawable.ic_search)
                    tag = R.drawable.ic_search
                }
            } else {
                binding.searchLayoutField.apply {
                    setEndIconDrawable(R.drawable.ic_clear)
                    tag = R.drawable.ic_clear
                }
            }
            viewModel.filterList(text.toString())
        }

        binding.searchLayoutField.setEndIconOnClickListener {
            if (binding.searchLayoutField.tag == R.drawable.ic_clear) {
                binding.searchEditText.setText("")
            }
        }
    }

    private fun setObservables() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: AreaChooserScreenState) {
        when (state) {
            is AreaChooserScreenState.Loading -> {
                showLoading()
            }

            is AreaChooserScreenState.Empty -> {
                showEmpty()
            }

            is AreaChooserScreenState.Content -> {
                showFoundAreas(state.items)
            }

            is AreaChooserScreenState.Error -> {
                showError()
            }
        }
    }

    private fun showLoading() {
        binding.rvArea.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showEmpty() {
        binding.placeholderImage.setImageResource(R.drawable.search_fail_placeholder)
        binding.placeholderMessage.text = getString(R.string.nothing_found_choosing_area_tv)
        binding.placeholderImage.contentDescription = getString(R.string.nothing_found_choosing_area_tv)
        binding.rvArea.isVisible = false
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun showFoundAreas(areas: List<Area>) {
        adapter.updateData(areas)
        binding.rvArea.isVisible = true
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.progressBar.isVisible = false
    }

    private fun showError() {
        binding.placeholderImage.setImageResource(R.drawable.region_search_fail_placeholder)
        binding.placeholderMessage.text = getString(R.string.search_throwable_choosing_area_tv)
        binding.placeholderImage.contentDescription = getString(R.string.search_throwable_choosing_area_tv)
        binding.rvArea.isVisible = false
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

