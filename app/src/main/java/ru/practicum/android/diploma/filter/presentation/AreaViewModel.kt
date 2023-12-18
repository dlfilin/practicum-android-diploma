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

        viewModelScope.launch {
            if (loadedFilter.country != null) {
                interactor.getAreasForId(loadedFilter.country?.id ?: "").collect {
                    loadAreaProcessResult(result = it)
                }
            } else {
                interactor.getAreas().collect {
                    loadAreaProcessResult(result = it)
                }
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
                _state.postValue(AreaChooserScreenState.Content(sortAreaByCapital(newList)))
            }
        } else {
            _state.postValue(AreaChooserScreenState.Content(sortAreaByCapital(areaList)))
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
                    _state.postValue(AreaChooserScreenState.Content(sortAreaByCapital(areaList)))
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
        val countriesId = countriesList.map { it.id }.toMutableList()
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
        return countriesList.firstOrNull { it.id == parentId }
    }

    private fun sortAreaByCapital(areaList: List<Area>): List<Area> {
        val newList = areaList.toMutableList()
        val others = areaList.minWith(Comparator.comparingInt { it.id.toInt() })
        newList.remove(others)
        newList.add(0, others)
        return newList
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 500L
    }
}
