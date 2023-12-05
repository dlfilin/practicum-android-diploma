package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding

class SimilarVacanciesFragment : Fragment(R.layout.fragment_similar_vacancies) {

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!


//    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSimilarVacanciesBinding.bind(view)

//        binding.gotoFilterFragmentBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
