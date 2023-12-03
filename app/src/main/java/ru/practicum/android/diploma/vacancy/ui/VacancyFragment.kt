package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.CustomAction
import ru.practicum.android.diploma.common.util.HasCustomActions
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment(R.layout.fragment_vacancy), HasCustomActions {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

//    private val args: VacancyFragmentArgs by navArgs()

//    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVacancyBinding.bind(view)

        binding.gotoSimilarJobsFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_vacancyFragment_to_similarJobsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun getCustomActions(): List<CustomAction> {
        return listOf(
            CustomAction(
                iconRes = R.drawable.ic_sharing,
                textRes = R.string.filter,
                onCustomAction = {
                    Toast.makeText(requireContext(), "Sharing", Toast.LENGTH_LONG).show()
                }
            ),
            CustomAction(
                iconRes = R.drawable.ic_favorite_inactiv,
                textRes = R.string.favorite_bottom_nav,
                onCustomAction = {
                    Toast.makeText(requireContext(), R.string.favorite_bottom_nav, Toast.LENGTH_LONG).show()
                }
            ),
        )
    }

}
