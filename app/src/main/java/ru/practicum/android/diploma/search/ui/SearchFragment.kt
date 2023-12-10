package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData
import ru.practicum.android.diploma.search.presentation.SearchScreenState
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.SearchAdapter

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val menuHost: MenuHost get() = requireActivity()
    private val viewmodel by viewModel<SearchViewModel>()

    private var isClickAllowed = true

    private val adapter = SearchAdapter(object : SearchAdapter.VacancyClickListener {
        override fun onVacancyClick(vacancy: VacancyItem) {
            if (clickDebounce()) {
                val direction = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.id)
                findNavController().navigate(direction)
            }
        }
    })
    private val onTrackClickDebounce = debounce<Boolean>(
        CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { param ->
        isClickAllowed = param
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        setTextWatcher()
        setRvAdapter()
        setObservables()
        prepareToolbarMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewmodel.checkIfFilterWasUpdated()
    }

    private fun setTextWatcher() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.searchLayoutField.apply {
                    setEndIconDrawable(R.drawable.ic_search)
                    tag = R.drawable.ic_search
                }
            } else {
                binding.searchLayoutField.apply {
                    setEndIconDrawable(R.drawable.ic_clear)
                    tag = R.drawable.ic_clear
                }
            }
            search()
        }

        binding.searchLayoutField.setEndIconOnClickListener {
            if (binding.searchLayoutField.tag == R.drawable.ic_clear) {
                binding.searchEditText.text?.clear()
            }
        }
    }

    private fun search() {
        viewmodel.searchDebounce(binding.searchEditText.text.toString())
    }

    private fun setRvAdapter() {
        binding.vacancyListRv.adapter = adapter
    }

    private fun prepareToolbarMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val item = menu.findItem(R.id.open_filter)
                val icon =
                    if (viewmodel.isFilterActive()) R.drawable.ic_filter_active else R.drawable.ic_filter_inactive
                item.setIcon(icon)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_filter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.open_filter -> {
                        findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setObservables() {
        viewmodel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewmodel.filterState.observe(viewLifecycleOwner) {
            // нужно дернуть тулбар в Activity, чтобы он перерисовался
            requireActivity().invalidateOptionsMenu()
        }
    }

    private fun renderState(it: SearchScreenState) {
        when (it) {
            is SearchScreenState.Default -> showDefault()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showFoundVacancy(it.vacancyData)
            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.InternetThrowable -> showInternetThrowable()
            is SearchScreenState.Error -> showError()
        }
    }

    private fun showDefault() {
        hideAllView()
        binding.placeholderImage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.placeholder_start_of_search)
    }

    private fun showLoading() {
        hideAllView()
        binding.newSearchProgressBar.isVisible = true
    }

    private fun showFoundVacancy(foundVacancyData: VacancyListData) {
        hideAllView()
        val numOfVacancy = resources.getQuantityString(
            R.plurals.vacancy_number,
            foundVacancyData.found,
            foundVacancyData.found
        )
        binding.vacanciesFound.text = numOfVacancy
        binding.vacanciesFound.isVisible = true
        adapter.setVacancyList(foundVacancyData.items)
        binding.nestedScrollRv.isVisible = true
        binding.vacanciesFound.isVisible = true
    }

    private fun showEmpty() {
        hideAllView()
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.placeholder_empty_result)
        binding.placeholderMessage.text = getString(R.string.load_vacancy_throwable_tv)
    }

    private fun showInternetThrowable() {
        hideAllView()
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
        binding.placeholderMessage.text = getString(R.string.internet_throwable_tv)
    }

    private fun showError() {
        hideAllView()
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageResource(R.drawable.placeholder_error_server)
        binding.placeholderMessage.text = getString(R.string.server_throwable_tv)
    }

    private fun hideAllView() {
        binding.vacanciesFound.isVisible = false
        binding.newSearchProgressBar.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.nestedScrollRv.isVisible = false
        binding.recyclerViewProgressBar.isVisible = false
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
