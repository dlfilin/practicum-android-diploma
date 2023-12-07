package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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
    private var isClickAllowed = true
    private val viewmodel by viewModel<SearchViewModel>()

    private val adapter = SearchAdapter(
        object : SearchAdapter.VacancyClickListener {
            override fun onVacancyClick(vacancy: VacancyItem) {
                if (clickDebounce()) {
                    findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment)
                    //передать id вакансии на экран информац3ии о вакансии
                }
            }
        }
    )
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

    private fun setTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // before text change watcher
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchEditText = binding.searchEditText
                if (s.isNullOrEmpty()) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
                    searchEditText.setOnTouchListener { view, motionEvent ->
                        val iconBoundries = searchEditText.compoundDrawables[2].bounds.width()
                        if (motionEvent.action == MotionEvent.ACTION_UP &&
                            motionEvent.rawX >= searchEditText.right - iconBoundries * 2
                        ) {
                            searchEditText.setText("")
                        }
                        view.performClick()
                        false
                    }
                    search()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // looking for the solution to delete underline decoration when typing the words
            }
        }

        simpleTextWatcher.let { binding.searchEditText.addTextChangedListener(it) }
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
                    if (true) R.drawable.ic_filter_active else R.drawable.ic_filter_inactive
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
            // нужно дернуть тулбар в Activity, чтобы он перерисовался
            requireActivity().invalidateOptionsMenu()

        }
    }

    private fun renderState(it: SearchScreenState) {
        when (it) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showFoundVacancy(it.vacancyData)
            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.InternetThrowable -> showInternetThrowable()
            is SearchScreenState.Error -> showError()
        }
    }

    private fun showInternetThrowable() {
        hideAllView()
        binding.internetThrowablePlaceholder.visibility = View.VISIBLE
        binding.internetThrowablePlaceHolderText.visibility = View.VISIBLE
    }

    private fun showError() {
        hideAllView()
        binding.serverThrowablePlaceholder.visibility = View.VISIBLE
        binding.serverThrowablePlaceholderText.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        hideAllView()
        binding.searchFailPlaceholder.visibility = View.VISIBLE
        binding.searchFailPlaceholderText.visibility = View.VISIBLE
    }

    private fun showFoundVacancy(foundVacancyData: VacancyListData) {
        hideAllView()
        val numOfVacancy = resources.getQuantityString(
            R.plurals.vacancy_number,
            foundVacancyData.found,
            foundVacancyData.found
        )
        binding.vacanciesFound.text = numOfVacancy
        binding.vacanciesFound.visibility = View.VISIBLE
        adapter.setVacancyList(foundVacancyData.items)
        binding.nestedScrollRv.visibility = View.VISIBLE
        binding.vacanciesFound.visibility = View.VISIBLE
    }

    private fun showLoading() {
        hideAllView()
        binding.newSearchProgressBar.visibility = View.VISIBLE
    }

    private fun hideAllView() {
        binding.vacanciesFound.visibility = View.GONE
        binding.newSearchProgressBar.visibility = View.GONE
        binding.searchVacancyPlaceholder.visibility = View.GONE
        binding.internetThrowablePlaceholder.visibility = View.GONE
        binding.internetThrowablePlaceHolderText.visibility = View.GONE
        binding.serverThrowablePlaceholder.visibility = View.GONE
        binding.serverThrowablePlaceholderText.visibility = View.GONE
        binding.searchFailPlaceholder.visibility = View.GONE
        binding.searchFailPlaceholderText.visibility = View.GONE
        binding.nestedScrollRv.visibility = View.GONE
        binding.recyclerViewProgressBar.visibility = View.GONE
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
