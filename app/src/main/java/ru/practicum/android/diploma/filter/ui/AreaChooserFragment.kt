package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentAreaChooserBinding
import ru.practicum.android.diploma.filter.presentation.AreaViewModel
import ru.practicum.android.diploma.filter.ui.adapters.AreaAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AreaChooserFragment : Fragment(R.layout.fragment_area_chooser) {

    private var _binding: FragmentAreaChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AreaViewModel by viewModel()

    private val adapter = AreaAdapter {
        val action =
            AreaChooserFragmentDirections.actionAreaChooserFragmentToWorkPlaceFragment("", it.name)
        findNavController().navigate(action)
    }

//    private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAreaChooserBinding.bind(view)

        binding.rvArea.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvArea.adapter = adapter

        lifecycleScope.launch {
            viewModel.getAreas().collect { areasList ->
                val areasSortedByName = areasList.sortedBy { it.name }
                adapter.updateData(areasSortedByName)
            }
        }

        binding.searchEditText.addTextChangedListener {
            adapter.filter(it.toString())
            if (adapter.listItem.isEmpty()) {
                binding.imageNotRegion.visibility = View.VISIBLE
                binding.textNotRegion.visibility = View.VISIBLE
            } else {
                binding.imageNotRegion.visibility = View.GONE
                binding.textNotRegion.visibility = View.GONE
            }
            val edText = binding.searchEditText
            if (!it.isNullOrEmpty()) {
                edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
                edText.setOnTouchListener { v, event ->
                    val iconBoundries = edText.compoundDrawables[2].bounds.width()
                    if (event.action == MotionEvent.ACTION_UP &&
                        event.rawX >= edText.right - iconBoundries * 2
                    ) {
                        edText.setText("")
                    }
                    view.performClick()
                    false
                }
            } else {
                edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

