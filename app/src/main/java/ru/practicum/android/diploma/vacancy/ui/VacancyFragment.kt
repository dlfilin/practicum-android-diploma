package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

class VacancyFragment : Fragment(R.layout.fragment_vacancy) {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private val menuHost: MenuHost get() = requireActivity()

    private val viewmodel by viewModel<VacancyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVacancyBinding.bind(view)

        binding.gotoSimilarJobsFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_vacancyFragment_to_similarVacanciesFragment)
        }

        setObservables()
        prepareToolbarMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun prepareToolbarMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val item = menu.findItem(R.id.action_toggle_favorite)
                val icon = if (viewmodel.isVacancyFavorite()) R.drawable.ic_favorite_active
                else R.drawable.ic_favorite_inactive
                item.setIcon(icon)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.vacancy_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        Toast.makeText(requireContext(), R.string.share_vacancy, Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.action_toggle_favorite -> {
                        viewmodel.toggleFavorite()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setObservables() {
        viewmodel.state.observe(viewLifecycleOwner) { state ->

            // нужно дернуть тулбар в Activity, чтобы он перерисовался
            requireActivity().invalidateOptionsMenu()

        }

    }

}
