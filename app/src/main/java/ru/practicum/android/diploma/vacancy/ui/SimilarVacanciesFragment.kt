package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesScreenState
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesViewModel

class SimilarVacanciesFragment : Fragment(R.layout.fragment_similar_vacancies) {

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!
    private val args: SimilarVacanciesFragmentArgs by navArgs()

    private val viewmodel by viewModel<SimilarVacanciesViewModel> {
        parametersOf(args.vacancyId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSimilarVacanciesBinding.bind(view)

        Toast.makeText(requireContext(), "vacancyId= ${args.vacancyId}", Toast.LENGTH_SHORT).show()

        setObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setObservables() {
        viewmodel.state.observe(viewLifecycleOwner) { state ->
            if (state is SimilarVacanciesScreenState.Content) {
                binding.name.text = state.vacancyData.items.toString()
            } else {
                binding.name.text = "XXX"
            }
        }
    }
}
