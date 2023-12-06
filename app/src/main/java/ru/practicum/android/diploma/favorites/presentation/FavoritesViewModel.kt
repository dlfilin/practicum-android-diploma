package ru.practicum.android.diploma.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Empty)
    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteState

    init {
        getVacancies()
    }

    fun getVacancies() {
        viewModelScope.launch {
            //TODO implement interactor usage to get all records
            processResults()
        }
    }

    private fun processResults() {
        renderState(FavoriteState.Empty)
    }

    private fun renderState(state: FavoriteState) {
        favoriteState.postValue(state)
    }
}
