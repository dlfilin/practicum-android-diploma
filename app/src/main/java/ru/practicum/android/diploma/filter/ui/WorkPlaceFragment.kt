package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding

class WorkPlaceFragment : Fragment(R.layout.fragment_work_place) {

    private var _binding: FragmentWorkPlaceBinding? = null
    private val binding get() = _binding!!

    private val actionArea = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToAreaChooserFragment()
    private val actionCountry = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToCountryChooserFragment()

    //    private val viewModel by viewModel<FilterViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWorkPlaceBinding.bind(view)

//        val args: WorkPlaceFragmentArgs by navArgs()
//        val textCountry = args.countryArgs
//        val textArea = args.areaArgs

//        binding.edTextNameCountryNameInput.setText(textCountry)
//        binding.edTextNameAreaNameInput.setText(textArea)
        addCountry()
        addArea()

        binding.btAdd.setOnClickListener {
            //записываем выбор в шаред префс и идем назад
            findNavController().navigateUp()
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
        binding.gotoCountryChooserFragmentBtn.setOnClickListener {
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
