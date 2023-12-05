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
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val menuHost: MenuHost get() = requireActivity()

    private val viewmodel by viewModel<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.gotoVacancyFragmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment)
        }

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
                            motionEvent.rawX >= searchEditText.right - iconBoundries * 2) {
                            searchEditText.setText("")
                        }
                        view.performClick()
                        false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // looking for the solution to delete underline decoration when typing the words
            }
        }

        simpleTextWatcher.let { binding.searchEditText.addTextChangedListener(it) }

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

            // нужно дернуть тулбар в Activity, чтобы он перерисовался
            requireActivity().invalidateOptionsMenu()

        }

    }

}
