package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.common.util.simpleScan
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData
import ru.practicum.android.diploma.search.presentation.FilterState
import ru.practicum.android.diploma.search.presentation.SearchScreenState
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.DefaultLoadStateAdapter
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import java.lang.Thread.State

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val menuHost: MenuHost get() = requireActivity()
    private val viewmodel by viewModel<SearchViewModel>()

    private var isClickAllowed = true
    private var filterIsActive = false

    private val onItemClickDebounce = debounce<Boolean>(
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
//        setRvAdapter()
//        checkFilterState()
        setToolbarMenu()
//        setObservables()


//        setVacanciesList()
//        setupSearchInput()
//        observeErrorMessages()

        val adapter = VacanciesAdapter(object : VacanciesAdapter.VacancyClickListener {
            override fun onVacancyClick(vacancy: VacancyItem) {
                if (clickDebounce()) {
                    val direction = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.id)
                    findNavController().navigate(direction)
                }
            }
        })
        binding.vacancyListRv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.itemsFlow.collectLatest {
                    adapter.submitData(it)
//                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Use the CombinedLoadStates provided by the loadStateFlow on the ArticleAdapter to
        // show progress bars when more data is being fetched
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect {

                    val state = it.source.append
                    if (state is LoadState.Error) {
                        when (state.error.message) {
                            "NO_INTERNET" -> Toast.makeText(requireContext(), "NO_INTERNET", Toast.LENGTH_SHORT).show()
                            "NON_200_RESPONSE" -> Toast.makeText(requireContext(), "NON_200_RESPONSE", Toast.LENGTH_SHORT).show()
                        }
                    } else if (state is LoadState.Loading) {
                        Toast.makeText(requireContext(), "LoadState.Loading", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "LoadState.NotLoading", Toast.LENGTH_SHORT).show()
                    }
                    binding.recyclerViewProgressBar.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
    }



//    private fun setVacanciesList() {
//        val adapter = VacanciesAdapter(object : VacanciesAdapter.VacancyClickListener {
//            override fun onVacancyClick(vacancy: VacancyItem) {
//                if (clickDebounce()) {
//                    val direction = SearchFragmentDirections.actionSearchFragmentToVacancyFragment(vacancy.id)
//                    findNavController().navigate(direction)
//                }
//            }
//        })
//
//        val footerAdapter = DefaultLoadStateAdapter()
//        val headerAdapter = DefaultLoadStateAdapter()
//
//        // combined adapter which shows both the list of Vacancies + footer indicator when loading pages
//        val adapterWithLoadState = adapter.withLoadStateHeaderAndFooter(headerAdapter, footerAdapter)
//
//        binding.vacancyListRv.adapter = adapterWithLoadState
////        (binding.vacancyListRv.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
//
//        observeVacancies(adapter)
//        observeLoadState(adapter)
//
//        handleScrollingToTopWhenSearching(adapter)
//        handleListVisibility(adapter)
//    }

//    private fun handleScrollingToTopWhenSearching(adapter: VacanciesAdapter) = lifecycleScope.launch {
//        // list should be scrolled to the 1st item (index = 0) if data has been reloaded:
//        // (prev state = Loading, current state = NotLoading)
//        getRefreshLoadStateFlow(adapter)
//            .simpleScan(count = 2)
//            .collectLatest { (previousState, currentState) ->
//                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
//                    binding.vacancyListRv.scrollToPosition(0)
//                }
//            }
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun handleListVisibility(adapter: VacanciesAdapter) = lifecycleScope.launch {
//        // list should be hidden if an error is displayed OR if items are being loaded after the error:
//        // (current state = Error) OR (prev state = Error)
//        //   OR
//        // (before prev state = Error, prev state = NotLoading, current state = Loading)
//        getRefreshLoadStateFlow(adapter)
//            .simpleScan(count = 3)
//            .collectLatest { (beforePrevious, previous, current) ->
//                binding.vacancyListRv.isInvisible = current is LoadState.Error
//                    || previous is LoadState.Error
//                    || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
//                    && current is LoadState.Loading)
//            }
//    }
//
//    private fun getRefreshLoadStateFlow(adapter: VacanciesAdapter): Flow<LoadState> {
//        return adapter.loadStateFlow
//            .map { it.refresh }
//    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewmodel.checkFilterState()
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
            viewmodel.setSearchBy(text.toString())
        }

        binding.searchLayoutField.setEndIconOnClickListener {
            if (binding.searchLayoutField.tag == R.drawable.ic_clear) {
                binding.searchEditText.text?.clear()
            }
        }
    }

//    private fun setRvAdapter() {
//        binding.vacancyListRv.adapter = adapter
//    }

    private fun setToolbarMenu() {
        menuHost.invalidateMenu()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val item = menu.findItem(R.id.open_filter)
                val icon =
                    if (filterIsActive) R.drawable.ic_filter_active else R.drawable.ic_filter_inactive
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

        viewmodel.filterState.observe(viewLifecycleOwner) { filterState ->
            renderFilterIcon(filterState)
        }
    }

    private fun renderFilterIcon(filterState: FilterState) {
        filterIsActive = filterState.isActive
        // нужно дернуть тулбар в Activity, чтобы он перерисовался
        requireActivity().invalidateOptionsMenu()
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
//        adapter.setVacancyList(foundVacancyData.items)
        binding.vacancyListRv.isVisible = true
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
        binding.vacancyListRv.isVisible = false
        binding.recyclerViewProgressBar.isVisible = false
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onItemClickDebounce(true)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
