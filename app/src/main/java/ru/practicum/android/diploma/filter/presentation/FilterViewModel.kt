package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class FilterViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private var editableFilter: FilterParameters = FilterParameters()
    private var isApplyBtnVisible = false

    private val _state = MutableLiveData<FilterScreenState>()
    val state: LiveData<FilterScreenState> get() = _state

    init {
        getFilterParameters()
        checkApplyBtnVisible()
        _state.postValue(
            FilterScreenState(
                editableFilter = editableFilter,
                isApplyBtnVisible = isApplyBtnVisible
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
        editableFilter = editableFilter.copy(salary = salary)
        saveEditableFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun clearWorkplace() {
        editableFilter = editableFilter.copy(area = null)
        saveEditableFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun clearIndustry() {
        editableFilter = editableFilter.copy(industry = null)
        saveEditableFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun clearSalary() {
        editableFilter = editableFilter.copy(salary = null)
        saveEditableFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun clearAll() {
        editableFilter = FilterParameters()
        saveEditableFilter()
        saveEditableInMainFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun updateSalary(text: String) {
        salaryDebounced(text)
    }

    fun onlyWithSalaryPressed(isChecked: Boolean) {
        editableFilter = editableFilter.copy(onlyWithSalary = isChecked)
        saveEditableFilter()
        checkApplyBtnVisible()
        updateState()
    }

    fun saveEditableInMainFilter() {
        interactor.saveEditableInMainFilter()
    }

    fun saveMainInEditableFilter() {
        interactor.saveMainInEditableFilter()
    }

    fun getFilterParameters() {
        editableFilter = interactor.getEditableFilter()
    }

    private fun checkApplyBtnVisible() {
        if (interactor.checkApplyBtnVisible() || editableFilter.isNotEmpty) {
            isApplyBtnVisible = true
        } else {
            false
        }
    }

    private fun updateState() {
        _state.postValue(FilterScreenState(isApplyBtnVisible, editableFilter))
    }

    private fun saveEditableFilter() {
        interactor.updateEditableFilter(editableFilter)
    }

    companion object {
        private const val SALARY_UPDATE_DELAY_IN_MILLIS = 1000L
    }
}
