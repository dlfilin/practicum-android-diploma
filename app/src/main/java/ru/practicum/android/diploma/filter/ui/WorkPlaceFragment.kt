package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.presentation.WorkPlaceViewModel

class WorkPlaceFragment : Fragment(R.layout.fragment_work_place) {

    private var _binding: FragmentWorkPlaceBinding? = null
    private val binding get() = _binding!!

    val viewModel: WorkPlaceViewModel by viewModel()
    private val directionArea = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToAreaChooserFragment()
    private val directionCountry = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToCountryChooserFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWorkPlaceBinding.bind(view)


        setListeners()

        viewModel.loadFilterFromPrefs()

        viewModel.state.observe(viewLifecycleOwner) { filter ->
            renderScreen(filter)
        }
    }

    private fun setListeners() {
        setCountryListeners()
        setAreaListeners()

        binding.btApply.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCountryListeners() {
        with(binding) {
            edTextNameCountryNameInput.setOnClickListener {
                findNavController().navigate(directionCountry)
            }

            edTextNameCountryNameInput.doOnTextChanged { text, _, _, _ ->
                edCountryName.apply {
                    tag = if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        R.drawable.ic_clear
                    }
                }
            }

            edCountryName.setEndIconOnClickListener {
                if (edCountryName.tag == R.drawable.ic_clear) {
                    viewModel.clearCountry()
                } else {
                    findNavController().navigate(directionCountry)
                }
            }

        }
    }

    private fun setAreaListeners() {
        with(binding) {
            edTextNameAreaNameInput.setOnClickListener {
                findNavController().navigate(directionArea)
            }

            edTextNameAreaNameInput.doOnTextChanged { text, _, _, _ ->
                edAreaName.apply {
                    tag = if (text.isNullOrEmpty()) {
                        setEndIconDrawable(R.drawable.ic_arrow_forward)
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        R.drawable.ic_clear
                    }
                }
            }

            edAreaName.setEndIconOnClickListener {
                if (edAreaName.tag == R.drawable.ic_clear) {
                    viewModel.clearArea()
                } else {
                    findNavController().navigate(directionArea)
                }
            }

        }
    }

    private fun renderScreen(filter: FilterParameters) {
        with(binding) {
            val country = filter.country?.name ?: ""
            val area = filter.area?.name ?: ""
            edTextNameCountryNameInput.setText(country)
            edTextNameAreaNameInput.setText(area)
            btApply.isVisible = country.isNotEmpty()
        }
    }

//    private fun addArrowArea() {
//        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
//            ?.let {
//                binding.edAreaName.setBoxStrokeColorStateList(it)
//                binding.edAreaName.defaultHintTextColor = it
//            }
//        binding.edAreaName.setEndIconDrawable(R.drawable.ic_arrow_forward)
//        binding.edTextNameAreaNameInput.setOnClickListener {
//            findNavController().navigate(directionArea)
//        }
//        binding.edTextNameAreaNameInput.setOnClickListener {
//            findNavController().navigate(directionArea)
//        }
//    }
//
//    private fun addArrowCountry() {
//        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
//            ?.let {
//                binding.edCountryName.setBoxStrokeColorStateList(it)
//                binding.edCountryName.defaultHintTextColor = it
//            }
//        binding.edCountryName.setEndIconDrawable(R.drawable.ic_arrow_forward)
//        binding.btAdd.visibility = View.GONE
//        binding.gotoCountryChooserFragmentBtn.setOnClickListener {
//            findNavController().navigate(directionCountry)
//        }
//        binding.edTextNameCountryNameInput.setOnClickListener {
//            findNavController().navigate(directionCountry)
//        }
//    }
//
//    private fun addArea() {
//        if (binding.edTextNameAreaNameInput.text.isNullOrBlank()) {
//            addArrowArea()
//        } else {
//            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
//                ?.let {
//                    binding.edAreaName.setBoxStrokeColorStateList(it)
//                    binding.edAreaName.defaultHintTextColor = it
//                }
//            binding.edAreaName.apply {
//                setEndIconDrawable(R.drawable.ic_clear)
//                tag = R.drawable.ic_clear
//            }
//        }
//        binding.edAreaName.setEndIconOnClickListener {
//            if (binding.edAreaName.tag == R.drawable.ic_clear) {
//                binding.edTextNameAreaNameInput.text?.clear()
//                binding.edAreaName.setEndIconDrawable(R.drawable.ic_arrow_forward)
//                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
//                    ?.let {
//                        binding.edAreaName.setBoxStrokeColorStateList(it)
//                        binding.edAreaName.defaultHintTextColor = it
//                    }
//                binding.edTextNameAreaNameInput.setOnClickListener {
//                    findNavController().navigate(directionArea)
//                }
//                binding.edAreaName.setEndIconOnClickListener {
//                    findNavController().navigate(directionArea)
//                }
//            }
//        }
//    }
//
//    private fun addCountry() {
//        if (binding.edTextNameCountryNameInput.text.isNullOrBlank()) {
//            addArrowCountry()
//        } else {
//            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
//                ?.let {
//                    binding.edCountryName.setBoxStrokeColorStateList(it)
//                    binding.edCountryName.defaultHintTextColor = it
//                }
//            binding.edCountryName.apply {
//                setEndIconDrawable(R.drawable.ic_clear)
//                tag = R.drawable.ic_clear
//                binding.btAdd.visibility = View.VISIBLE
//            }
//        }
//        binding.edCountryName.setEndIconOnClickListener {
//            if (binding.edCountryName.tag == R.drawable.ic_clear) {
//                binding.edTextNameCountryNameInput.text?.clear()
//                binding.edCountryName.setEndIconDrawable(R.drawable.ic_arrow_forward)
//                AppCompatResources.getColorStateList(requireContext(), R.color.gray)
//                    ?.let {
//                        binding.edCountryName.setBoxStrokeColorStateList(it)
//                        binding.edCountryName.defaultHintTextColor = it
//                    }
//                binding.btAdd.visibility = View.GONE
//                binding.edTextNameCountryNameInput.setOnClickListener {
//                    findNavController().navigate(directionCountry)
//                }
//                binding.edCountryName.setEndIconOnClickListener {
//                    findNavController().navigate(directionCountry)
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveFilterToPrefs()
    }
}
