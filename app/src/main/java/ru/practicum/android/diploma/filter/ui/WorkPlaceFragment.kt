package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.presentation.FilterScreenState
import ru.practicum.android.diploma.filter.presentation.WorkPlaceViewModel

class WorkPlaceFragment : Fragment(R.layout.fragment_work_place) {

    private var _binding: FragmentWorkPlaceBinding? = null
    private val binding get() = _binding!!

    private val actionArea = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToAreaChooserFragment()
    private val actionCountry = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToCountryChooserFragment()

    private val viewModel by viewModel<WorkPlaceViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWorkPlaceBinding.bind(view)

        viewModel.getFilterParameters()

        addCountry()
        addArea()

        binding.btAdd.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        // переопределяем нажатие кнопки назад в тулбаре чтобы возвращаться только на один экран назад
        val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun renderState(state: FilterScreenState) {
        with(binding) {
            edTextNameCountryNameInput.setText(state.editableFilter.country?.name ?: "")
            edTextNameAreaNameInput.setText(state.editableFilter.area?.name ?: "")
            btAdd.isVisible = state.isApplyBtnVisible
        }
    }

    private fun addArrowArea() {
        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            ?.let {
                binding.edAreaName.setBoxStrokeColorStateList(it)
                binding.edAreaName.defaultHintTextColor = it
            }
        binding.edAreaName.setEndIconDrawable(R.drawable.ic_arrow_forward)
        binding.edTextNameAreaNameInput.setOnClickListener {
            findNavController().navigate(actionArea)
        }
        binding.edTextNameAreaNameInput.setOnClickListener {
            findNavController().navigate(actionArea)
        }
    }

    private fun addArrowCountry() {
        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
            ?.let {
                binding.edCountryName.setBoxStrokeColorStateList(it)
                binding.edCountryName.defaultHintTextColor = it
            }
        binding.edCountryName.setEndIconDrawable(R.drawable.ic_arrow_forward)
        binding.btAdd.visibility = View.GONE
        binding.edCountryName.setOnClickListener {
            findNavController().navigate(actionCountry)
        }
        binding.edTextNameCountryNameInput.setOnClickListener {
            findNavController().navigate(actionCountry)
        }
    }

    private fun addArea() {
        if (binding.edTextNameAreaNameInput.text.isNullOrBlank()) {
            addArrowArea()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    binding.edAreaName.setBoxStrokeColorStateList(it)
                    binding.edAreaName.defaultHintTextColor = it
                }
            binding.edAreaName.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
            }
        }
        binding.edAreaName.setEndIconOnClickListener {
            if (binding.edAreaName.tag == R.drawable.ic_clear) {
                binding.edTextNameAreaNameInput.text?.clear()
                binding.edAreaName.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                    ?.let {
                        binding.edAreaName.setBoxStrokeColorStateList(it)
                        binding.edAreaName.defaultHintTextColor = it
                    }
                binding.edTextNameAreaNameInput.setOnClickListener {
                    findNavController().navigate(actionArea)
                }
                binding.edAreaName.setEndIconOnClickListener {
                    findNavController().navigate(actionArea)
                }
            }
        }
    }

    private fun addCountry() {
        if (binding.edTextNameCountryNameInput.text.isNullOrBlank()) {
            addArrowCountry()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    binding.edCountryName.setBoxStrokeColorStateList(it)
                    binding.edCountryName.defaultHintTextColor = it
                }
            binding.edCountryName.apply {
                setEndIconDrawable(R.drawable.ic_clear)
                tag = R.drawable.ic_clear
                binding.btAdd.visibility = View.VISIBLE
            }
        }
        binding.edCountryName.setEndIconOnClickListener {
            if (binding.edCountryName.tag == R.drawable.ic_clear) {
                binding.edTextNameCountryNameInput.text?.clear()
                binding.edCountryName.setEndIconDrawable(R.drawable.ic_arrow_forward)
                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                    ?.let {
                        binding.edCountryName.setBoxStrokeColorStateList(it)
                        binding.edCountryName.defaultHintTextColor = it
                    }
                binding.btAdd.visibility = View.GONE
                binding.edTextNameCountryNameInput.setOnClickListener {
                    findNavController().navigate(actionCountry)
                }
                binding.edCountryName.setEndIconOnClickListener {
                    findNavController().navigate(actionCountry)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
