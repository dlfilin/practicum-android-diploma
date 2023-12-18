package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class AreaViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private var areaList = emptyList<Area>()
    private var loadedFilter = FilterParameters()
    private var countriesList = emptyList<Country>()

    val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { changedText ->
        filterList(changedText)
    }

    private val _state = MutableLiveData<AreaChooserScreenState>(AreaChooserScreenState.Loading)
    val state: LiveData<AreaChooserScreenState> get() = _state

    init {
        loadedFilter = interactor.getCurrentFilter()

        if (loadedFilter.country != null) {
            viewModelScope.launch {
                interactor.getAreasForId(loadedFilter.country?.id ?: "").collect {
                    loadAreaProcessResult(result = it)
                }
            }
        } else {
            viewModelScope.launch {
                interactor.getAreas().collect {
                    loadAreaProcessResult(result = it)
                }
            }

            viewModelScope.launch {
                interactor.getCountries().collect {
                    loadCountriesProcessResult(result = it)
                }
            }
        }
    }

    private fun filterList(text: String) {
        if (text.isNotBlank()) {
            val newList = areaList.filter { area ->
                area.name.lowercase().trim().contains(text.lowercase().trim())
            }
            if (newList.isEmpty()) {
                _state.postValue(AreaChooserScreenState.Empty)
            } else {
                _state.postValue(AreaChooserScreenState.Content(newList))
            }
        } else {
            _state.postValue(AreaChooserScreenState.Content(areaList))
        }
    }

    fun saveFilterToPrefs(area: Area) {
        if (loadedFilter.country == null) {
            val countryId = getCountryIdFromArea(area)
            val country = getCountryById(countryId)
            loadedFilter = loadedFilter.copy(country = country)
        }
        val filter = loadedFilter.copy(area = area)
        interactor.updateFilter(filter)
    }

    private fun loadAreaProcessResult(result: NetworkResult<List<Area>>) {
        when (result) {
            is NetworkResult.Success -> {
                val data = result.data!!
                if (data.isEmpty()) {
                    _state.postValue(AreaChooserScreenState.Empty)
                } else {
                    areaList = data
                    _state.postValue(AreaChooserScreenState.Content(areaList))
                }
            }

            is NetworkResult.Error -> {
                _state.postValue(AreaChooserScreenState.Error)
            }
        }
    }

    private fun loadCountriesProcessResult(result: NetworkResult<List<Country>>) {
        when (result) {
            is NetworkResult.Success -> {
                countriesList = result.data!!
            }

            is NetworkResult.Error -> {
                _state.postValue(AreaChooserScreenState.Error)
            }
        }
    }

    private fun getCountryIdFromArea(selectedArea: Area): String {
        val countriesId = mutableListOf<String>()
        for (country in countriesList) {
            countriesId.add(country.id)
        }
        var parentId = selectedArea.parentId
        while (true) {
            if (countriesId.contains(parentId)) {
                return parentId
            } else {
                parentId = areaList.firstOrNull { it.id == parentId }?.parentId ?: ""
            }
        }
    }

    private fun getCountryById(parentId: String): Country? {
        for (country in countriesList) {
            if (country.id == parentId) {
                return country
            }
        }
        return null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 500L
    }
}
