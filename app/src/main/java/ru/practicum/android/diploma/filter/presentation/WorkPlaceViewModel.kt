package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class WorkPlaceViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private var editableFilter: FilterParameters = FilterParameters()

    private val _state = MutableLiveData<FilterScreenState>()
    val state: LiveData<FilterScreenState> get() = _state

    fun getFilterParameters() {
        editableFilter = interactor.getEditableFilter()
        updateState()
    }

    private fun updateState() {
        _state.postValue(FilterScreenState(true, editableFilter))
    }

}
