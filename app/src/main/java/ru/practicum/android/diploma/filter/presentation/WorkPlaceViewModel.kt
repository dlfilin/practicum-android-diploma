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
        val filter = state.value!!.copy(country = null)
        _state.postValue(filter)
    }

    fun clearArea() {
        val filter = state.value!!.copy(area = null)
        _state.postValue(filter)
    }

    fun saveFilterToPrefs() {
        val filter = state.value!!
        interactor.updateFilter(filter)
    }

    fun loadFilterFromPrefs() {
        val filter = interactor.getCurrentFilter()
        _state.postValue(filter)
        // _state.postValue(checkAreaBelongsCountry(filter))
    }

//    private fun checkAreaBelongsCountry(filter: FilterParameters): FilterParameters {
//        return if (filter.country?.id != filter.area?.countryId) {
//            filter.copy(area = null)
//        } else filter
//    }
}
