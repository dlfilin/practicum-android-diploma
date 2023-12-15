package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout.END_ICON_CUSTOM
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.FilterScreenState
import ru.practicum.android.diploma.filter.presentation.FilterViewModel

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    val viewModel: FilterViewModel by viewModel()
    private val directionWorkPlace = FilterFragmentDirections.actionFilterFragmentToWorkPlaceFragment()
    private val directionIndustry = FilterFragmentDirections.actionFilterFragmentToIndustryChooserFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFilterBinding.bind(view)

        viewModel.getFilterParameters()

        setListeners()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilterParameters()
    }

    private fun setListeners() {
        setWorkPlaceListeners()
        setIndustryListeners()
        setSalaryListeners()
        setOnBackPressed()

        with(binding) {
            checkBoxSalary.setOnClickListener {
                viewModel.onlyWithSalaryPressed(checkBoxSalary.isChecked)
            }

            btApply.setOnClickListener {
                viewModel.saveEditableInMainFilter()
                findNavController().navigateUp()
            }

            btClear.setOnClickListener {
                viewModel.clearAll()
            }
        }
    }

    private fun setWorkPlaceListeners() {
        with(binding) {
            edWorkPlace.setOnClickListener {
                findNavController().navigate(directionWorkPlace)
            }

            edWorkPlace.doOnTextChanged { text, _, _, _ ->
                edWorkPlaceLayout.apply {
                    tag = if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        R.drawable.ic_clear
                    }
                }
            }

            edWorkPlaceLayout.setEndIconOnClickListener {
                if (edWorkPlaceLayout.tag == R.drawable.ic_clear) {
                    viewModel.clearWorkplace()
                } else {
                    findNavController().navigate(directionWorkPlace)
                }
            }

        }
    }

    private fun setIndustryListeners() {
        with(binding) {
            edIndustry.setOnClickListener {
                findNavController().navigate(directionIndustry)
            }

            edIndustry.doOnTextChanged { text, _, _, _ ->
                edIndustryLayout.apply {
                    tag = if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        R.drawable.ic_clear
                    }
                }
            }

            edIndustryLayout.setEndIconOnClickListener {
                if (edIndustryLayout.tag == R.drawable.ic_clear) {
                    viewModel.clearIndustry()
                } else {
                    findNavController().navigate(directionIndustry)
                }
            }
        }
    }

    private fun setSalaryListeners() {
        with(binding) {
            textInputEditTextSalary.doOnTextChanged { text, _, _, _ ->
                textInputLayoutSalary.apply {
                    if (text.isNullOrEmpty()) {
                        endIconMode = END_ICON_NONE
                    } else {
                        endIconMode = END_ICON_CUSTOM
                        setEndIconDrawable(R.drawable.ic_clear)
                    }
                }
                viewModel.updateSalary(text.toString())
            }

            textInputLayoutSalary.setEndIconOnClickListener {
                viewModel.clearSalary()
            }
        }
    }

    private fun setOnBackPressed() {
        // переопределяем нажатие кнопки назад в тулбаре
        val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            viewModel.saveMainInEditableFilter()
            findNavController().navigateUp()
            activity?.onBackPressed()
        }

        // переопределяем нажатие системной кнопки назад
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.saveMainInEditableFilter()
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun renderState(state: FilterScreenState) {
        with(binding) {
            // TODO получение строки потом переделать
            val place = if (state.editableFilter.country != null) {
                (state.editableFilter.country?.name ?: "") + ", " + (state.editableFilter.area?.name ?: "")
            } else {
                (state.editableFilter.area?.name ?: "")
            }
            val industry = state.editableFilter.industry?.name ?: ""
            val salary = state.editableFilter.salary?.toString() ?: ""
            edWorkPlace.setText(place)
            edIndustry.setText(industry)
            textInputEditTextSalary.setText(salary)
            checkBoxSalary.isChecked = state.editableFilter.onlyWithSalary
            btApply.isVisible = state.isApplyBtnVisible
            btClear.isVisible = state.isClearBtnVisible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
