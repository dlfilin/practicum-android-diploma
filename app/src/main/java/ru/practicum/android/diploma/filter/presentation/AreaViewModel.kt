package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Area

class AreaViewModel(private val interactor: FilterInteractor) : ViewModel() {

    fun getAreas(): Flow<List<Area>> = interactor.getAreas()

}
