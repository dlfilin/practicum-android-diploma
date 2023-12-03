package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.CustomAction
import ru.practicum.android.diploma.common.util.HasCustomActions
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search), HasCustomActions {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


//    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.gotoFilterFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
        binding.gotoVacancyFragmentBtn.setOnClickListener {
            // потом здесь добавим передачу через safeArgs
            val direction = SearchFragmentDirections.actionSearchFragmentToVacancyFragment()
            findNavController().navigate(direction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun getCustomActions(): List<CustomAction> {
        return listOf(
            CustomAction(
                iconRes = R.drawable.ic_filter_inactive,
                textRes = R.string.filter,
                onCustomAction = {
                    Toast.makeText(requireContext(), R.string.filter, Toast.LENGTH_LONG).show()
                }
            )
        )
    }

}
