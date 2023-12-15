package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class FilterViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private val _state = MutableLiveData<FilterScreenState>()
    val state: LiveData<FilterScreenState> get() = _state

    init {
        val filter = interactor.getCurrentFilter()
        _state.postValue(
            FilterScreenState(
                entryFilter = filter,
                currentFilter = filter
            )
        )
    }

    private val salaryDebounced = debounce<String>(
        SALARY_UPDATE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { text ->
        val salary = if (text == "") {
            null
        } else {
            text.toInt()
        }
        val filter = state.value!!.currentFilter.copy(salary = salary)
        updateState(filter)
    }

    fun clearWorkplace() {
        val filter = state.value!!.currentFilter.copy(area = null)
        updateState(filter)
    }

    fun clearIndustry() {
        val filter = state.value!!.currentFilter.copy(industry = null)
        updateState(filter)
    }

    fun clearSalary() {
        val filter = state.value!!.currentFilter.copy(salary = null)
        updateState(filter)
    }

    fun clearAll() {
        val filter = FilterParameters()
        updateState(filter)
    }

    fun updateSalary(text: String) {
        salaryDebounced(text)
    }

    fun onlyWithSalaryPressed(isChecked: Boolean) {
        val filter = state.value!!.currentFilter.copy(onlyWithSalary = isChecked)
        updateState(filter)
    }

    private fun updateState(filter: FilterParameters) {
        val st = state.value!!.copy(currentFilter = filter)
        _state.postValue(st)
    }

    fun saveFilter() {
        val filter = state.value!!.currentFilter
        interactor.updateFilter(filter)
    }
//
//    fun getIndustryAndSaveDb() {
//        viewModelScope.launch {
//            interactor.getIndustryAndSaveDb()
//        }
//    }
//
//    fun getCountryAndSaveDb() {
//        viewModelScope.launch {
//            interactor.getCountryAndSaveDb()
//        }
//    }
//
//    fun getAreaAndSaveDb() {
//        viewModelScope.launch {
//            interactor.getAreaAndSaveDb()
//        }
//    }

    companion object {
        private const val SALARY_UPDATE_DELAY_IN_MILLIS = 1000L
    }

}
