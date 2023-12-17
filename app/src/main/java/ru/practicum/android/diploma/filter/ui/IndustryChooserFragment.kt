package ru.practicum.android.diploma.filter.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.databinding.FragmentIndustryChooserBinding
import ru.practicum.android.diploma.filter.presentation.IndustryChooserScreenState
import ru.practicum.android.diploma.filter.presentation.IndustryViewModel
import ru.practicum.android.diploma.filter.ui.adapters.IndustryAdapter

class IndustryChooserFragment : Fragment(R.layout.fragment_industry_chooser) {

    private var _binding: FragmentIndustryChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IndustryViewModel by viewModel()

    private val adapter = IndustryAdapter { item ->
        if (clickDebounce()) {
            viewModel.industrySelected(item)
            hideSoftKeyboard()
        }
    }

    private var isClickAllowed = true
    private val onTrackClickDebounce = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { param ->
        isClickAllowed = param
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIndustryChooserBinding.bind(view)

        binding.rvIndustry.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIndustry.adapter = adapter
        binding.rvIndustry.itemAnimator = null

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        setListeners()
    }

    private fun setListeners() {
        binding.btApply.setOnClickListener {
            viewModel.saveFilterToPrefs()
            findNavController().navigateUp()
        }

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
            viewModel.applySearchResults(text.toString())
        }

        binding.searchLayoutField.setEndIconOnClickListener {
            if (binding.searchLayoutField.tag == R.drawable.ic_clear) {
                binding.searchEditText.text?.clear()
                binding.searchEditText.requestFocus()
            }
        }
    }

    private fun renderState(state: IndustryChooserScreenState) {
        when (state) {
            is IndustryChooserScreenState.Content -> {
                adapter.updateData(state.items)
                binding.rvIndustry.isVisible = true
                binding.placeholderImage.isVisible = false
                binding.placeholderMessage.isVisible = false
                binding.btApply.isVisible = state.items.any { it.isChecked }
            }

            is IndustryChooserScreenState.Error -> {
                binding.rvIndustry.isVisible = false
                binding.placeholderImage.isVisible = true
                binding.placeholderMessage.isVisible = true
                binding.btApply.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        binding.searchEditText.isEnabled = true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
