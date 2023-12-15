package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Area

class AreaViewModel(private val interactor: FilterInteractor) : ViewModel() {

    init {
        viewModelScope.launch {
            interactor.getAreaAndSaveDb()
        }
    }

    fun getAreas(): Flow<List<Area>> = interactor.getAreas()

    fun saveArea(area: Area) {
        interactor.saveArea(area)
    }
}
