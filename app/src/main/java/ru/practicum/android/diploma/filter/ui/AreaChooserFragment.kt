package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaChooserBinding
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

        binding.rvArea.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvArea.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

//        binding.searchEditText.addTextChangedListener {
//            adapter.filter(it.toString())
//            if (adapter.listItem.isEmpty()) {
//                binding.imageNotRegion.visibility = View.VISIBLE
//                binding.textNotRegion.visibility = View.VISIBLE
//            } else {
//                binding.imageNotRegion.visibility = View.GONE
//                binding.textNotRegion.visibility = View.GONE
//            }
//            val edText = binding.searchEditText
//            if (!it.isNullOrEmpty()) {
//                edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
//                edText.setOnTouchListener { v, event ->
//                    val iconBoundries = edText.compoundDrawables[2].bounds.width()
//                    if (event.action == MotionEvent.ACTION_UP &&
//                        event.rawX >= edText.right - iconBoundries * 2
//                    ) {
//                        edText.setText("")
//                    }
//                    view.performClick()
//                    false
//                }
//            } else {
//                edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
//            }
//        }
    }

    private fun renderState(state: AreaChooserScreenState) {
        when (state) {
            is AreaChooserScreenState.Content -> {
                adapter.updateData(state.items)
                binding.rvArea.isVisible = true
                binding.placeholderImage.isVisible = false
                binding.placeholderMessage.isVisible = false
            }
            is AreaChooserScreenState.Error -> {
                binding.rvArea.isVisible = false
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

