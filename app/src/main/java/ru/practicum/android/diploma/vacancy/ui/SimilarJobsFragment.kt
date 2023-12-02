package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarJobsBinding

class SimilarJobsFragment : Fragment(R.layout.fragment_similar_jobs) {

    private var _binding: FragmentSimilarJobsBinding? = null
    private val binding get() = _binding!!


//    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSimilarJobsBinding.bind(view)

//        binding.gotoFilterFragmentBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
