package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Industry

class IndustryViewModel(private val interactor: FilterInteractor) : ViewModel() {
    // TODO для теста
    init {
        viewModelScope.launch {
            interactor.getIndustryAndSaveDb()
        }
    }

    fun getIndustries(): Flow<List<Industry>> = interactor.getIndustries()

    fun saveIndustry(industry: Industry) {
        interactor.saveIndustry(industry)
    }
}
