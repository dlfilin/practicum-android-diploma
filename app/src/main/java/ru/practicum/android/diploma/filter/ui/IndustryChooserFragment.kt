package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryChooserBinding
import ru.practicum.android.diploma.filter.presentation.IndustryChooserScreenState
import ru.practicum.android.diploma.filter.presentation.IndustryViewModel
import ru.practicum.android.diploma.filter.ui.adapters.IndustryAdapter

class IndustryChooserFragment : Fragment(R.layout.fragment_industry_chooser) {

    private var _binding: FragmentIndustryChooserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IndustryViewModel by viewModel()

    private val adapter = IndustryAdapter {
        // ПОКА БЕЗ ВЫБОРА, ПО АНАЛОГИИ РЕГИОНАМ
        // ПОТОМ СОХРАНЕНИЕ НА КНОПКУ
        viewModel.saveFilterToPrefs(it)
        findNavController().navigateUp()
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

//        binding.btApply.setOnClickListener {
//            viewModel.saveFilterToPrefs()
//        }

//        val textWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Log.d("testTextWatcher", "$s")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                adapter.filter(s?.toString() ?: " ")
//                visibleBtAdd(adapter.listItem)
//
//                val edText = binding.searchEditText
//                if (!s.isNullOrEmpty()) {
//                    edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
//                    edText.setOnTouchListener { v, event ->
//                        val iconBoundries = edText.compoundDrawables[2].bounds.width()
//                        if (event.action == MotionEvent.ACTION_UP &&
//                            event.rawX >= edText.right - iconBoundries * 2
//                        ) {
//                            edText.setText("")
//                        }
//                        view.performClick()
//                        false
//                    }
//                } else {
//                    edText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                Log.d("testTextWatcher", "$s")
//            }
//        }
//        binding.searchEditText.addTextChangedListener(textWatcher)
    }

//    fun visibleBtAdd(list: List<Industry>) {
//        val result = list.find { it.isChecked }
//        if (result != null) {
//            binding.btAdd.visibility = View.VISIBLE
//        } else {
//            binding.btAdd.visibility = View.GONE
//        }
//    }

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
}
