package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _state = MutableLiveData(SearchScreenState())
    val state: LiveData<SearchScreenState> get() = _state

    fun isFilterActive() = state.value?.isFilterActive ?: false

}
