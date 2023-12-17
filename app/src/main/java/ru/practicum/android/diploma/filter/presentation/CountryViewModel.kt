package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country

class CountryViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private val _state = MutableLiveData<CountryChooserScreenState>()
    val state: LiveData<CountryChooserScreenState> get() = _state

    init {
        viewModelScope.launch {
            interactor.getCountries().collect {
                processResult(result = it)
            }
        }
    }

    private fun processResult(result: NetworkResult<List<Country>>) {
        when (result) {
            is NetworkResult.Success -> {
                val data = result.data!!.sortedBy { it.name.length }
                _state.postValue(CountryChooserScreenState.Content(data))

            }

            is NetworkResult.Error -> {
                _state.postValue(CountryChooserScreenState.Error)
            }
        }
    }

    fun saveFilterToPrefs(country: Country) {
        val filter = interactor.getCurrentFilter().copy(country = country)
        interactor.updateFilter(filter)
    }
}
