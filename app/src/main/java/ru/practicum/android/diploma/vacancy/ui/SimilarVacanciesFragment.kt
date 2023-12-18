package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesScreenState
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.vacancy.ui.adapter.SimilarAdapter

class SimilarVacanciesFragment : Fragment(R.layout.fragment_similar_vacancies) {

    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    private val args: SimilarVacanciesFragmentArgs by navArgs()
    private val adapter = SimilarAdapter {
        if (clickDebounce()) {
            val action = SimilarVacanciesFragmentDirections.actionSimilarVacanciesFragmentToVacancyFragment(it.id)
            findNavController().navigate(action)
        }
    }

    private val viewModel by viewModel<SimilarVacanciesViewModel> {
        parametersOf(args.vacancyId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSimilarVacanciesBinding.bind(view)

        addRvAdapter()
        setObservables()
    }

    private fun setObservables() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun addRvAdapter() {
        binding.rvSimilar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSimilar.adapter = adapter
        binding.rvSimilar.setHasFixedSize(false)
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
        placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
        placeholderMessage.text = getString(R.string.internet_throwable_tv)
        updateScreenViews(
            isSimilarRvVisible = false,
            isMainProgressVisible = false,
            isPlaceholderVisible = true
        )
    }

    private fun showError() = with(binding) {
        placeholderImage.setImageResource(R.drawable.placeholder_error_server)
        placeholderMessage.text = getString(R.string.server_throwable_tv)
        updateScreenViews(
            isSimilarRvVisible = false,
            isMainProgressVisible = false,
            isPlaceholderVisible = true
        )
    }

    private fun showEmpty() = with(binding) {
        placeholderImage.setImageResource(R.drawable.placeholder_empty_result)
        placeholderMessage.text = getString(R.string.load_vacancy_throwable_tv)
        updateScreenViews(
            isSimilarRvVisible = false,
            isMainProgressVisible = false,
            isPlaceholderVisible = true
        )
    }

    private fun showLoading() {
        updateScreenViews(
            isSimilarRvVisible = false,
            isMainProgressVisible = true,
            isPlaceholderVisible = false
        )
    }

    private fun showContent(list: List<VacancyItem>) = with(binding) {
        adapter.addSimilarList(list)
        updateScreenViews(
            isSimilarRvVisible = true,
            isMainProgressVisible = false,
            isPlaceholderVisible = false
        )
    }

    private fun updateScreenViews(
        isSimilarRvVisible: Boolean,
        isMainProgressVisible: Boolean,
        isPlaceholderVisible: Boolean,
    ) {
        with(binding) {
            similarProgressBar.isVisible = isMainProgressVisible
            placeholderImage.isVisible = isPlaceholderVisible
            placeholderMessage.isVisible = isPlaceholderVisible
            rvSimilar.isVisible = isSimilarRvVisible
        }
    }

    private val onClickDebounce = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { param ->
        isClickAllowed = param
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onClickDebounce(true)
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
