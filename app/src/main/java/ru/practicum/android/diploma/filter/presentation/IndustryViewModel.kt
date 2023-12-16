package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.presentation.models.IndustryUi

class IndustryViewModel(private val interactor: FilterInteractor) : ViewModel() {


    private val _state = MutableLiveData<IndustryChooserScreenState>()
    val state: LiveData<IndustryChooserScreenState> get() = _state

    init {
        viewModelScope.launch {
            interactor.getIndustries().collect {
                processResult(result = it)
            }
        }
    }

    private fun processResult(result: NetworkResult<List<Industry>>) {
        when (result) {
            is NetworkResult.Success -> {
                val data = result.data!!.map {
                    IndustryUi(
                        id = it.id,
                        name = it.name
                    )
                }
                _state.postValue(IndustryChooserScreenState.Content(data))

            }

            is NetworkResult.Error -> {
                _state.postValue(IndustryChooserScreenState.Error)
            }
        }
    }

    fun saveFilterToPrefs(industry: IndustryUi) {
        //val industry = (state.value as IndustryChooserScreenState.Content).items.first { it.isChecked }
        val filter = interactor.getCurrentFilter().copy(
            industry = Industry(
                id = industry.id,
                name = industry.name
            )
        )
        interactor.updateFilter(filter)
    }

}
