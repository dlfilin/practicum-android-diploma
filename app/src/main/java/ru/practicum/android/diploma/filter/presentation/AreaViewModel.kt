package ru.practicum.android.diploma.filter.presentation

import android.util.Log
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
        viewModelScope.launch {
            interactor.getAreas().collect {
                loadAreaProcessResult(result = it)
            }
        }

        loadedFilter = interactor.getCurrentFilter()

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
        if (newList.isEmpty()) {
            _state.postValue(AreaChooserScreenState.Empty)
        } else {
            _state.postValue(AreaChooserScreenState.Content(newList))
        }
    }

    fun saveFilterToPrefs(area: Area) {
        if (loadedFilter.country == null) {
            val countryId = getCountryIdFromArea(area)
            val country = getCountryById(countryId)
            loadedFilter = loadedFilter.copy(country = country)
        }
        val filter = loadedFilter.copy(area = area)
        Log.d("algo", "saratov: ${filter.area.toString()}")
        interactor.updateFilter(filter)
    }

    private fun loadAreaProcessResult(result: NetworkResult<List<Area>>) {
        when (result) {
            is NetworkResult.Success -> {
                val data = result.data!!
                if (data.isEmpty()) {
                    _state.postValue(AreaChooserScreenState.Empty)
                } else {
                    Log.d("area1", data.toString())
                    areaList = if (countryIsNotEmpty) {
                        filterAreaByCountry(data)
                    } else {
                        data
                    }
                    Log.d("area1", areaList.size.toString())
                    _state.postValue(AreaChooserScreenState.Content(areaList.sortedBy { it.name }))
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

    private fun filterAreaByCountry(areas: List<Area>): List<Area> {
//        val finalAreaList = mutableListOf<Area>()
//        var id = loadedFilter.country?.id
//        val areasId = mutableListOf<String>()
//        var finalAreaSize = 0
//
//        while (true) {
//            finalAreaSize = finalAreaList.size
//            for (area in areas) {
//                if (area.parentId == id) {
//                    if (!finalAreaList.contains(area)) {
//                        finalAreaList.add(area)
//                    }
//                }
//            }
//
//            if (finalAreaSize == finalAreaList.size) {
//                break
//            }
//
//            for (item in finalAreaList) {
//                if (!areasId.contains(item.id)) {
//                    id = item.id
//                    areasId.add(id)
//                    break
//                }
//            }
//        }
        return areas
    }
}
