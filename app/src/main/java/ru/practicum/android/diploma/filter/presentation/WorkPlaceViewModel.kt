package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class WorkPlaceViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private val _state = MutableLiveData<FilterParameters>()
    val state: LiveData<FilterParameters> get() = _state

    fun clearCountry() {
        val filter = state.value!!.copy(country = null, area = null)
        _state.postValue(filter)
    }

    fun clearArea() {
        val filter = state.value!!.copy(area = null)
        _state.postValue(filter)
    }

    fun saveFilterToLocalStorage() {
        val filter = state.value!!
        interactor.updateFilter(filter)
    }

    fun loadFilterFromLocalStorage() {
        val filter = interactor.getCurrentFilter()
        _state.postValue(filter)
    }
}
