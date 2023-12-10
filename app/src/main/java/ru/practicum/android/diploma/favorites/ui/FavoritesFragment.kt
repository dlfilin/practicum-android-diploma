package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoriteState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.favorites.ui.adapter.VacancyAdapter
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private val adapter = VacancyAdapter(object : VacancyAdapter.OnClickListener {
        override fun onVacancyClick(vacancy: Vacancy) {
            if (clickDebounce()) {
                val direction = FavoritesFragmentDirections.actionFavoritesFragmentToVacancyFragment(vacancy.id)
                findNavController().navigate(direction)
            }
        }
    })

    private var isClickAllowed = true
    private val onTrackClickDebounce = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { param ->
        isClickAllowed = param
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.favoritesRecyclerView.adapter = adapter
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Error -> showError()
            is FavoriteState.Loading -> showLoading()
            is FavoriteState.Content -> showContent(state.vacancies)
        }
    }

    private fun hideAll() {
        binding.apply {
            favoritesPlaceholder.isVisible = false
            favoritesPlaceholderText.isVisible = false
            favoritesRecyclerView.isVisible = false
            favoritesProgressBar.isVisible = false
        }
    }

    private fun showLoading() {
        hideAll()
        binding.favoritesProgressBar.isVisible = true
    }

    private fun showEmpty() {
        hideAll()
        binding.apply {
            favoritesPlaceholder.setImageResource(R.drawable.placeholder_favorite_empty)
            favoritesPlaceholderText.setText(R.string.nothing_shown_vacancy_tv)
            favoritesPlaceholder.isVisible = true
            favoritesPlaceholderText.isVisible = true
        }
    }

    private fun showError() {
        binding.apply {
            favoritesPlaceholder.setImageResource(R.drawable.placeholder_empty_result)
            favoritesPlaceholderText.setText(R.string.load_vacancy_throwable_tv)
            favoritesPlaceholder.isVisible = true
            favoritesPlaceholderText.isVisible = true
        }
    }

    private fun showContent(vacancies: List<Vacancy>) {
        hideAll()
        adapter.updateRecycleView(vacancies)
        binding.favoritesRecyclerView.isVisible = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVacancies()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
