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
import ru.practicum.android.diploma.databinding.FragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.presentation.WorkPlaceViewModel

class WorkPlaceFragment : Fragment(R.layout.fragment_work_place) {

    private var _binding: FragmentWorkPlaceBinding? = null
    private val binding get() = _binding!!

    val viewModel: WorkPlaceViewModel by viewModel()
    private val directionArea = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToAreaChooserFragment()
    private val directionCountry = WorkPlaceFragmentDirections.actionWorkPlaceFragmentToCountryChooserFragment()
    private var filterHintStateEmpty: ColorStateList? = null
    private var filterHintStateFilled: ColorStateList? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWorkPlaceBinding.bind(view)
        filterHintStateEmpty = AppCompatResources.getColorStateList(requireContext(), R.color.filter_item_empty)
        filterHintStateFilled = AppCompatResources.getColorStateList(requireContext(), R.color.filter_item_filled)

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
                        defaultHintTextColor = filterHintStateEmpty
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        defaultHintTextColor = filterHintStateFilled
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
                        defaultHintTextColor = filterHintStateEmpty
                        R.drawable.ic_arrow_forward
                    } else {
                        setEndIconDrawable(R.drawable.ic_clear)
                        defaultHintTextColor = filterHintStateFilled
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveFilterToPrefs()
    }
}
