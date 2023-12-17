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
        getInitialData()
    }

    private fun getInitialData() {
        viewModelScope.launch {
            interactor.getIndustries().collect {
                processResult(result = it)
            }
        }
    }

    private fun processResult(result: NetworkResult<List<Industry>>) {
        when (result) {
            is NetworkResult.Success -> {
                val filter = interactor.getCurrentFilter()
                val data = result.data!!.map {
                    IndustryUi(
                        id = it.id,
                        name = it.name,
                        isChecked = (it.id == filter.industry?.id)
                    )
                }

                _state.postValue(IndustryChooserScreenState.Content(data))

            }

            is NetworkResult.Error -> {
                _state.postValue(IndustryChooserScreenState.Error)
            }
        }
    }

    fun saveFilterToPrefs() {
        val industry = (state.value as IndustryChooserScreenState.Content).items.first { it.isChecked }
        val filter = interactor.getCurrentFilter().copy(
            industry = Industry(
                id = industry.id,
                name = industry.name
            )
        )
        interactor.updateFilter(filter)
    }

    fun industrySelected(industry: IndustryUi) {
        val currentList = state.value as IndustryChooserScreenState.Content
        val updatedList = currentList.items.map {
            IndustryUi(
                id = it.id,
                name = it.name,
                isChecked = (it.id == industry.id)
            )
        }
        _state.postValue(IndustryChooserScreenState.Content(updatedList))
    }

    fun applySearchResults(search: String?) {
        if (search.isNullOrEmpty()) {
            getInitialData()
        } else if (state.value is IndustryChooserScreenState.Content) {
            val currentList = state.value as IndustryChooserScreenState.Content
            val updatedList = mutableListOf<IndustryUi>()
            currentList.items.map {
                if (it.name.contains(search, true)) {
                    updatedList.add(it)
                }
            }
            _state.postValue(IndustryChooserScreenState.Content(updatedList))
        }
    }
}
