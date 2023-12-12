package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.FilterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    val viewModel: FilterViewModel by viewModel()
    private val actionWorkPlace = FilterFragmentDirections.actionFilterFragmentToWorkPlaceFragment()
    private val actionIndustry = FilterFragmentDirections.actionFilterFragmentToIndustryChooserFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFilterBinding.bind(view)

        lifecycle.coroutineScope.launch {
            viewModel.getIndustryAndSaveDb()
        }

        val args: FilterFragmentArgs by navArgs()
        val countryName = args.countryArgs
        val areaName = args.areaArgs
        if (countryName == "" && areaName == "") {
            binding.edWorkPlace.setText("")
        } else if (countryName != "" && areaName == "") {
            binding.edWorkPlace.setText(countryName)
        } else if (countryName != "") {
            val textWorkPlace = "$countryName, $areaName"
            binding.edWorkPlace.setText(textWorkPlace)
        }

        listenSalaryEditText()
        checkBoxSalary()


        val industryName = args.industryArgs
        binding.edIndustry.setText(industryName)

        addWorkPlace()
        addIndustry()

        binding.btClear.setOnClickListener {
            showDefault()
            binding.btClear.isVisible = false
            binding.btAdd.isVisible = false
        }
    }

    private fun addArrowWorkPlace() = with(binding) {
        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            ?.let {
                edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                edWorkPlaceLayout.defaultHintTextColor = it
            }
        edWorkPlaceLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
        edWorkPlace.setOnClickListener {
            findNavController().navigate(actionWorkPlace)
        }
        edWorkPlace.setOnClickListener {
            findNavController().navigate(actionWorkPlace)
        }
    }

    private fun addWorkPlace() = with(binding) {
        if (edWorkPlace.text.isNullOrBlank()) {
            addArrowWorkPlace()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                    edWorkPlaceLayout.defaultHintTextColor = it
                }
            edWorkPlaceLayout.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
                btClear.isVisible = true
                btAdd.isVisible = true
            }
        }
        edWorkPlaceLayout.setEndIconOnClickListener {
            if (edWorkPlaceLayout.tag == R.drawable.ic_clear) {
                edWorkPlace.text?.clear()
                edWorkPlaceLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                    ?.let {
                        edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                        edWorkPlaceLayout.defaultHintTextColor = it
                    }
                edWorkPlace.setOnClickListener {
                    findNavController().navigate(actionWorkPlace)
                }
                edWorkPlaceLayout.setEndIconOnClickListener {
                    findNavController().navigate(actionWorkPlace)
                }
            }
        }
    }

    private fun addArrowIndustry() = with(binding) {
        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            ?.let {
                edIndustryLayout.setBoxStrokeColorStateList(it)
                edIndustryLayout.defaultHintTextColor = it
            }
        edIndustryLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
        edIndustry.setOnClickListener {
            findNavController().navigate(actionIndustry)
        }
        edIndustry.setOnClickListener {
            findNavController().navigate(actionIndustry)
        }
    }

    private fun addIndustry() = with(binding) {
        if (edIndustry.text.isNullOrBlank()) {
            addArrowIndustry()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    edIndustryLayout.setBoxStrokeColorStateList(it)
                    edIndustryLayout.defaultHintTextColor = it
                }
            edIndustryLayout.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
                btClear.isVisible = true
                btAdd.isVisible = true
            }
        }
        edIndustryLayout.setEndIconOnClickListener {
            if (edIndustryLayout.tag == R.drawable.ic_clear) {
                edIndustry.text?.clear()
                edIndustryLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                    ?.let {
                        edIndustryLayout.setBoxStrokeColorStateList(it)
                        edIndustryLayout.defaultHintTextColor = it
                    }
                edIndustry.setOnClickListener {
                    findNavController().navigate(actionIndustry)
                }
                edIndustryLayout.setEndIconOnClickListener {
                    findNavController().navigate(actionIndustry)
                }
            }
        }
    }

    private fun showDefault() = with(binding) {
        edWorkPlace.text?.clear()
        edIndustry.text?.clear()
        textInputEditTextSalary.text?.clear()
        checkBoxSalary.isChecked = false
        btClear.isVisible = false
        btAdd.isVisible = false
    }

    private fun listenSalaryEditText() = with(binding) {
        textInputEditTextSalary.addTextChangedListener {
            if (!edIndustryLayout.isEmpty()) {
                btClear.isVisible = true
                btAdd.isVisible = true
            }
        }

        textInputLayoutSalary.setEndIconOnClickListener {
            textInputEditTextSalary.text?.clear()
            if (edIndustry.text.isNullOrEmpty() && edWorkPlace.text.isNullOrEmpty()
                && textInputEditTextSalary.text.isNullOrEmpty()
            ) {
                btClear.isVisible = false
                btAdd.isVisible = false
            }

        }
    }

    private fun checkBoxSalary() = with(binding) {
        checkBoxSalary.setOnClickListener {
            if (checkBoxSalary.isChecked) {
                btClear.isVisible = true
                btAdd.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
