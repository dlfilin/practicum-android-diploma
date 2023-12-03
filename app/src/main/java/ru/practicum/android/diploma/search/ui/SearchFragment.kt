package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding


class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


//    private val viewModel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.gotoFilterFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filter_graph)
        }
        binding.gotoVacancyFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_vacancy_graph)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
                }
                else {
                    binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
                    binding.searchEditText.setOnTouchListener {view, motionEvent ->
                        if ((motionEvent.action == MotionEvent.ACTION_UP) &&
                            (motionEvent.rawX >= (binding.searchEditText.right - binding.searchEditText.compoundDrawables[2].bounds.width() * 2))
                        ) {
                            binding.searchEditText.setText("")
                        }
                        view.performClick()
                        false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        simpleTextWatcher.let { binding.searchEditText.addTextChangedListener(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
