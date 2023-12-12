package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesScreenState
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.vacancy.ui.adapter.SimilarAdapter

class SimilarVacanciesFragment : Fragment(R.layout.fragment_similar_vacancies) {

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!
    private val args: SimilarVacanciesFragmentArgs by navArgs()
    private val adapter = SimilarAdapter {
        val action = SimilarVacanciesFragmentDirections.actionSimilarVacanciesFragmentToVacancyFragment(it.id)
        findNavController().navigate(action)
    }

    private val viewModel by viewModel<SimilarVacanciesViewModel> {
        parametersOf(args.vacancyId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSimilarVacanciesBinding.bind(view)

        binding.rvSimilar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSimilar.adapter = adapter

        setObservables()
    }

    private fun setObservables() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun render(state: SimilarVacanciesScreenState) {
        when (state) {
            is SimilarVacanciesScreenState.Content -> showContent(state.vacancyData.items)
            is SimilarVacanciesScreenState.Loading -> showLoading()
            is SimilarVacanciesScreenState.Empty -> showEmpty()
            is SimilarVacanciesScreenState.Error -> showError()
            is SimilarVacanciesScreenState.InternetThrowable -> showInternetThrowable()
        }

    }

    private fun showInternetThrowable() = with(binding) {
        placeholderImage.isVisible = true
        placeholderMessage.isVisible = true
        similarProgressBar.isVisible = false
        placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
        placeholderMessage.text = getString(R.string.internet_throwable_tv)
    }

    private fun showError() = with(binding) {
        placeholderImage.isVisible = true
        placeholderMessage.isVisible = true
        similarProgressBar.isVisible = false
        placeholderImage.setImageResource(R.drawable.placeholder_error_server)
        placeholderMessage.text = getString(R.string.server_throwable_tv)
    }

    private fun showEmpty() = with(binding) {
        placeholderImage.isVisible = true
        placeholderMessage.isVisible = true
        similarProgressBar.isVisible = false
        placeholderImage.setImageResource(R.drawable.placeholder_empty_result)
        placeholderMessage.text = getString(R.string.load_vacancy_throwable_tv)
    }

    private fun showLoading() = with(binding) {
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        similarProgressBar.isVisible = true
    }

    private fun showContent(list: List<VacancyItem>) = with(binding) {
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        similarProgressBar.isVisible = false
        adapter.addSimilarList(list)
    }
}
