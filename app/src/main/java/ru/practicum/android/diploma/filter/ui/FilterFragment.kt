package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val actionWorkPlace = FilterFragmentDirections.actionFilterFragmentToWorkPlaceFragment()
    private val actionIndustry = FilterFragmentDirections.actionFilterFragmentToIndustryChooserFragment()

//    private val viewModel by viewModel<FilterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFilterBinding.bind(view)

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

        val industryName = args.industryArgs
        binding.edIndustry.setText(industryName)

        addWorkPlace()
        addIndustry()
    }

    private fun addWorkPlace() {
        if (binding.edWorkPlace.text.isNullOrBlank()) {
            AppCompatResources.getColorStateList(requireContext(), R.color.gray)?.let {
                    binding.edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                    binding.edWorkPlaceLayout.defaultHintTextColor = it
                }
            binding.edWorkPlaceLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
            binding.edWorkPlace.setOnClickListener {
                findNavController().navigate(actionWorkPlace)
            }
            binding.edWorkPlace.setOnClickListener {
                findNavController().navigate(actionWorkPlace)
            }
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)?.let {
                    binding.edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                    binding.edWorkPlaceLayout.defaultHintTextColor = it
                }
            binding.edWorkPlaceLayout.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
            }
        }
        binding.edWorkPlaceLayout.setEndIconOnClickListener {
            if (binding.edWorkPlaceLayout.tag == R.drawable.ic_clear) {
                binding.edWorkPlace.text?.clear()
                binding.edWorkPlaceLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)?.let {
                        binding.edWorkPlaceLayout.setBoxStrokeColorStateList(it)
                        binding.edWorkPlaceLayout.defaultHintTextColor = it
                    }
                binding.edWorkPlace.setOnClickListener {
                    findNavController().navigate(actionWorkPlace)
                }
                binding.edWorkPlaceLayout.setEndIconOnClickListener {
                    findNavController().navigate(actionWorkPlace)
                }
            }
        }
    }

    private fun addIndustry() {
        if (binding.edIndustry.text.isNullOrBlank()) {
            AppCompatResources.getColorStateList(requireContext(), R.color.gray)?.let {
                    binding.edIndustryLayout.setBoxStrokeColorStateList(it)
                    binding.edIndustryLayout.defaultHintTextColor = it
                }
            binding.edIndustryLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
            binding.edIndustry.setOnClickListener {
                findNavController().navigate(actionIndustry)
            }
            binding.edIndustry.setOnClickListener {
                findNavController().navigate(actionIndustry)
            }
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)?.let {
                    binding.edIndustryLayout.setBoxStrokeColorStateList(it)
                    binding.edIndustryLayout.defaultHintTextColor = it
                }
            binding.edIndustryLayout.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
            }
        }
        binding.edIndustryLayout.setEndIconOnClickListener {
            if (binding.edIndustryLayout.tag == R.drawable.ic_clear) {
                binding.edIndustry.text?.clear()
                binding.edIndustryLayout.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)?.let {
                        binding.edIndustryLayout.setBoxStrokeColorStateList(it)
                        binding.edIndustryLayout.defaultHintTextColor = it
                    }
                binding.edIndustry.setOnClickListener {
                    findNavController().navigate(actionIndustry)
                }
                binding.edIndustryLayout.setEndIconOnClickListener {
                    findNavController().navigate(actionIndustry)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
