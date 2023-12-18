package ru.practicum.android.diploma.filter.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.presentation.FilterViewModel

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    val viewModel: FilterViewModel by viewModel()
    private val directionWorkPlace = FilterFragmentDirections.actionFilterFragmentToWorkPlaceFragment()
    private val directionIndustry = FilterFragmentDirections.actionFilterFragmentToIndustryChooserFragment()
    private var colorStateEmpty: ColorStateList? = null
    private var colorStateFilled: ColorStateList? = null
    private var filterHintStateEmpty: ColorStateList? = null
    private var filterHintStateFilled: ColorStateList? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFilterBinding.bind(view)

        colorStateEmpty = AppCompatResources.getColorStateList(requireContext(), R.color.salary_field_empty)
        colorStateFilled = AppCompatResources.getColorStateList(requireContext(), R.color.salary_field_filled)
        filterHintStateEmpty = AppCompatResources.getColorStateList(requireContext(), R.color.filter_item_empty)
        filterHintStateFilled = AppCompatResources.getColorStateList(requireContext(), R.color.filter_item_filled)

        setListeners()

        viewModel.loadFilterFromPrefs()

        viewModel.state.observe(viewLifecycleOwner) { filter ->
            renderScreen(filter)
        }
    }

    private fun setListeners() {
        setWorkPlaceListeners()
        setIndustryListeners()
        setSalaryListeners()

        with(binding) {
            checkBoxSalary.setOnClickListener {
                viewModel.onlyWithSalaryPressed(checkBoxSalary.isChecked)
            }

            btApply.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    REAPPLY_FILTER,
                    true
                )
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
                    if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        defaultHintTextColor = filterHintStateEmpty
                        tag = R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        defaultHintTextColor = filterHintStateFilled
                        tag = R.drawable.ic_clear
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
                    if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        defaultHintTextColor = filterHintStateEmpty
                        tag = R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        defaultHintTextColor = filterHintStateFilled
                        tag = R.drawable.ic_clear
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
        var prevText = ""
        with(binding) {
            textInputEditTextSalary.doOnTextChanged { text, _, _, _ ->
                if (text.toString() != prevText) {
                    if (text.isNullOrEmpty()) {
                        textInputLayoutSalary.defaultHintTextColor = colorStateEmpty
                    } else {
                        textInputLayoutSalary.defaultHintTextColor = colorStateFilled
                    }
                    prevText = text.toString()
                    viewModel.updateSalary(prevText)
                }
            }
        }
    }

    private fun renderScreen(filter: FilterParameters) {
        with(binding) {
            val place = prepareCountryString(filter.country, filter.area)
            val industry = filter.industry?.name ?: ""
            val salary = filter.salary?.toString() ?: ""
            edWorkPlace.setText(place)
            edIndustry.setText(industry)
            textInputEditTextSalary.setText(salary)
            textInputEditTextSalary.setSelection(salary.length)
            checkBoxSalary.isChecked = filter.onlyWithSalary
            btApply.isVisible = filter.isNotEmpty
            btClear.isVisible = filter.isNotEmpty
        }
    }

    private fun prepareCountryString(country: Country?, area: Area?): String {
        val place: String = if (country != null) {
            if (area != null) {
                "${country.name}, ${area.name}"
            } else {
                country.name
            }
        } else {
            area?.name ?: ""
        }
        return place
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveFilterToPrefs()
    }

    companion object {
        const val REAPPLY_FILTER = "REAPPLY_FILTER"
    }
}
