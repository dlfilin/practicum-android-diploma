package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.ErrorType
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
    private var isFilterActive = false

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

        setListeners()
        setRvAdapter()
        checkFilterState()
        setToolbarMenu()
        setObservables()
    }

    override fun onResume() {
        super.onResume()
        if (!binding.searchEditText.text.isNullOrBlank()) {
            viewmodel.applyFilter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
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
            viewmodel.searchTextChanged(text.toString())
        }

        binding.searchLayoutField.setEndIconOnClickListener {
            if (binding.searchLayoutField.tag == R.drawable.ic_clear) {
                binding.searchEditText.text?.clear()
            }
        }

        binding.vacancyListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.vacancyListRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewmodel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun setRvAdapter() {
        binding.vacancyListRv.adapter = adapter
        // для приятного скроллинга
        binding.vacancyListRv.setHasFixedSize(false)
    }

    private fun setToolbarMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val item = menu.findItem(R.id.open_filter)
                val icon = if (isFilterActive) R.drawable.ic_filter_active else R.drawable.ic_filter_inactive
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

    private fun checkFilterState() {
        viewmodel.checkFilterState()
    }

    private fun setObservables() {
        viewmodel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewmodel.isFilterActiveState.observe(viewLifecycleOwner) { isActive ->
            renderFilterIcon(isActive)
        }

        viewmodel.toastEvent.observe(viewLifecycleOwner) { error ->
            when (error) {
                ErrorType.NO_INTERNET -> showToast(getString(R.string.toast_internet_throwable))
                else -> showToast(getString(R.string.toast_unknown_error))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun renderFilterIcon(isActive: Boolean) {
        isFilterActive = isActive
        // нужно дернуть тулбар в Activity, чтобы он перерисовался
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderState(it: SearchScreenState) {
        when (it) {
            is SearchScreenState.Default -> showDefault()
            is SearchScreenState.InitialLoading -> showInitialLoading()
            is SearchScreenState.Content -> showFoundVacancy(it.vacancyData)
            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.Error -> showError(it.error)
            is SearchScreenState.NextPageLoading -> showNextPageLoadingProgress(it.isLoading)
        }
    }

    private fun showDefault() {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_start_of_search)
        updateScreenViews(
            isMainProgressVisible = false,
            isSearchRvVisible = false,
            isPlaceholderVisible = true,
            isVacanciesFoundVisible = false,
            isNextPageLoadingVisible = false
        )
        binding.placeholderMessage.isVisible = false
    }

    private fun showInitialLoading() {
        updateScreenViews(
            isMainProgressVisible = true,
            isSearchRvVisible = false,
            isPlaceholderVisible = false,
            isVacanciesFoundVisible = false,
            isNextPageLoadingVisible = false
        )
    }

    private fun showFoundVacancy(foundVacancyData: VacancyListData) {
        val numOfVacancy = resources.getQuantityString(
            R.plurals.vacancy_number,
            foundVacancyData.found,
            foundVacancyData.found
        )
        binding.vacanciesFound.text = numOfVacancy
        adapter.submitList(foundVacancyData.items)
        if (foundVacancyData.page == 0) binding.vacancyListRv.scrollToPosition(0)
        updateScreenViews(
            isMainProgressVisible = false,
            isSearchRvVisible = true,
            isPlaceholderVisible = false,
            isVacanciesFoundVisible = true,
            isNextPageLoadingVisible = false
        )
    }

    private fun showEmpty() {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_empty_result)
        binding.placeholderMessage.text = getString(R.string.load_vacancy_throwable_tv)
        binding.vacanciesFound.text = getString(R.string.no_vacancies_found_title)
        updateScreenViews(
            isMainProgressVisible = false,
            isSearchRvVisible = false,
            isPlaceholderVisible = true,
            isVacanciesFoundVisible = true,
            isNextPageLoadingVisible = false
        )
    }

    private fun showError(error: ErrorType) {
        if (error == ErrorType.NO_INTERNET) {
            binding.placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
            binding.placeholderMessage.text = getString(R.string.internet_throwable_tv)
        } else {
            binding.placeholderImage.setImageResource(R.drawable.placeholder_error_server)
            binding.placeholderMessage.text = getString(R.string.server_throwable_tv)
        }
        updateScreenViews(
            isMainProgressVisible = false,
            isSearchRvVisible = false,
            isPlaceholderVisible = true,
            isVacanciesFoundVisible = false,
            isNextPageLoadingVisible = false
        )
    }

    private fun showNextPageLoadingProgress(isLoading: Boolean) {
        updateScreenViews(
            isMainProgressVisible = false,
            isSearchRvVisible = true,
            isPlaceholderVisible = false,
            isVacanciesFoundVisible = true,
            isNextPageLoadingVisible = isLoading
        )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(true)
        }
        return current
    }

    private fun updateScreenViews(
        isMainProgressVisible: Boolean,
        isSearchRvVisible: Boolean,
        isPlaceholderVisible: Boolean,
        isVacanciesFoundVisible: Boolean,
        isNextPageLoadingVisible: Boolean
    ) {
        with(binding) {
            vacanciesFound.isVisible = isVacanciesFoundVisible
            mainProgressBar.isVisible = isMainProgressVisible
            placeholderImage.isVisible = isPlaceholderVisible
            placeholderMessage.isVisible = isPlaceholderVisible
            vacancyListRv.isVisible = isSearchRvVisible
            recyclerViewProgressBar.isVisible = isNextPageLoadingVisible
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
