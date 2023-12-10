package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

        val args: WorkPlaceFragmentArgs by navArgs()
        val textCountry = args.countryArgs
        val textArea = args.areaArgs

        binding.edTextNameCountryNameInput.setText(textCountry)
        binding.edTextNameAreaNameInput.setText(textArea)
        addCountry()
        addArea()
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
        if (binding.edTextNameAreaNameInput.text.isNullOrBlank()) { addArrowArea()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    binding.edAreaName.setBoxStrokeColorStateList(it)
                    binding.edAreaName.defaultHintTextColor = it
                }
            binding.edAreaName.setEndIconDrawable(R.drawable.ic_clear)
            binding.edTextNameAreaNameInput.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawable = binding.edTextNameAreaNameInput.compoundDrawables[2]
                    if (drawable != null && event.rawX >=
                        binding.edTextNameAreaNameInput.right - drawable.bounds.width()) {
                        binding.edTextNameAreaNameInput.setText("")
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                            ?.let {
                                binding.edAreaName.setBoxStrokeColorStateList(it)
                                binding.edAreaName.defaultHintTextColor = it
                            }
                        binding.edAreaName.setEndIconDrawable(R.drawable.ic_arrow_forward)
                        binding.edTextNameAreaNameInput.setOnClickListener {
                            findNavController().navigate(actionArea)
                        }
                        binding.edAreaName.setEndIconOnClickListener {
                            findNavController().navigate(actionArea)
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            binding.edTextNameAreaNameInput.requestFocus()
            binding.edTextNameAreaNameInput.clearFocus()
        }
    }

    private fun addCountry() {
        if (binding.edTextNameCountryNameInput.text.isNullOrBlank()) { addArrowCountry()
        } else {
            AppCompatResources.getColorStateList(requireContext(), R.color.black_universal)
                ?.let {
                    binding.edCountryName.setBoxStrokeColorStateList(it)
                    binding.edCountryName.defaultHintTextColor = it
                }
            binding.edCountryName.setEndIconDrawable(R.drawable.ic_clear)
            binding.btAdd.visibility = View.VISIBLE
            binding.edTextNameCountryNameInput.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawable = binding.edTextNameCountryNameInput.compoundDrawables[2]
                    if (drawable != null && event.rawX >=
                        binding.edTextNameCountryNameInput.right - drawable.bounds.width()) {
                        binding.edTextNameCountryNameInput.setText("")
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                            ?.let {
                                binding.edCountryName.setBoxStrokeColorStateList(it)
                                binding.edCountryName.defaultHintTextColor = it
                            }
                        binding.edCountryName.setEndIconDrawable(R.drawable.ic_arrow_forward)
                        binding.btAdd.visibility = View.GONE
                        binding.edTextNameCountryNameInput.setOnClickListener {
                            findNavController().navigate(actionCountry)
                        }
                        binding.edCountryName.setEndIconOnClickListener {
                            findNavController().navigate(actionCountry)
                        }
                        true
                    } else { false
                    }
                } else { false
                }
            }
            binding.edTextNameCountryNameInput.requestFocus()
            binding.edTextNameCountryNameInput.clearFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
