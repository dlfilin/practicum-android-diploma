package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class AreaViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private var areaList = emptyList<Area>()
    private var loadedFilter = FilterParameters()
    private var countriesList = emptyList<Country>()
    private var countryIsNotEmpty = false

    private val _state = MutableLiveData<AreaChooserScreenState>(AreaChooserScreenState.Loading)
    val state: LiveData<AreaChooserScreenState> get() = _state

    init {
        loadedFilter = interactor.getCurrentFilter()

        viewModelScope.launch {
            interactor.getAreas().collect {
                loadAreaProcessResult(result = it)
            }
        }

        if (loadedFilter.country == null) {
            viewModelScope.launch {
                interactor.getCountries().collect {
                    loadCountriesProcessResult(result = it)
                }
            }
        } else {
            countryIsNotEmpty = true
        }
    }

    fun filterList(text: String) {
        val newList = areaList.filter { area ->
            area.name.lowercase().trim().contains(text.lowercase().trim())
        }
        _state.postValue(AreaChooserScreenState.Content(newList))
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
                    areaList = if (countryIsNotEmpty) {
                        filterAreaByCountry(data)
                    } else {
                        data
                    }
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
        var parentId = selectedArea.parentId
        while (true) {
            for (area in areaList) {
                if (area.id == parentId) {
                    parentId = area.parentId
                } else {
                    return parentId
                }
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

    private fun filterAreaByCountry(areas: List<Area>): List<Area> {
        val countryId = loadedFilter.country?.id
        val newAreaList = mutableListOf<Area>()
        for (area in areas) {
            if (area.parentId == countryId) {
                newAreaList.add(area)
            }
        }
        for (area in newAreaList) {
            for (item in areas) {
                if (area.id == item.parentId) {
                    newAreaList.add(item)
                }
            }
        }
        return newAreaList.toList()
    }
}
