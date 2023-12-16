package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Area

class AreaViewModel(private val interactor: FilterInteractor) : ViewModel() {

    private val _state = MutableLiveData<AreaChooserScreenState>()
    val state: LiveData<AreaChooserScreenState> get() = _state

    init {
        viewModelScope.launch {
            interactor.getAreas().collect {
                processResult(result = it)
            }
        }
    }

    private fun processResult(result: NetworkResult<List<Area>>) {
        when (result) {
            is NetworkResult.Success -> {
                val data = result.data!!
                _state.postValue(AreaChooserScreenState.Content(data))

            }

            is NetworkResult.Error -> {
                _state.postValue(AreaChooserScreenState.Error)
            }
        }
    }

    fun saveFilterToPrefs(area: Area) {
        val filter = interactor.getCurrentFilter().copy(area = area)
        interactor.updateFilter(filter)
    }

}
