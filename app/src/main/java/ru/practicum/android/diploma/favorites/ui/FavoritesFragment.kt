package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.favorites.presentation.FavoriteState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private val adapter = VacancyAdapter(object : VacancyAdapter.OnClickListener {
        override fun onVacancyClick(vacancy: Favorite) {
            findNavController().navigate(R.id.action_favoritesFragment_to_vacancyFragment)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.favoritesRecyclerView.adapter = adapter

        /*binding.gotoVacancyFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_vacancyFragment)
        }*/
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Error -> showError()
            is FavoriteState.Content -> showContent(state.vacancies)
        }
    }

    private fun showEmpty() {
        with(binding) {
            favoritesPlaceholder.setImageResource(R.drawable.placeholder_favorite_empty)
            favoritesPlaceholder.visibility = View.VISIBLE
            favoritesPlaceholderText.setText(R.string.nothing_shown_vacancy_tv)
            favoritesPlaceholderText.visibility = View.VISIBLE
            favoritesRecyclerView.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            favoritesPlaceholder.setImageResource(R.drawable.placeholder_empty_result)
            favoritesPlaceholder.visibility = View.VISIBLE
            favoritesPlaceholderText.setText(R.string.load_vacancy_throwable_tv)
            favoritesPlaceholderText.visibility = View.VISIBLE
            favoritesRecyclerView.visibility = View.GONE
        }
    }

    private fun showContent(vacancies: List<Favorite>) {
        with(binding) {
            favoritesPlaceholder.visibility = View.GONE
            favoritesPlaceholderText.visibility = View.GONE
            adapter.updateRecycleView(vacancies)
            favoritesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVacancies()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
