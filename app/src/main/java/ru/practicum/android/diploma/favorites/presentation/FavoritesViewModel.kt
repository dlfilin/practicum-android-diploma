package ru.practicum.android.diploma.favorites.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor

class FavoritesViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Empty)
    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteState

    init {
        getVacancies()
    }

    fun getVacancies() {
        viewModelScope.launch {
            try {
                favoriteInteractor.getAll().collect { vacancies ->
                    if (vacancies.isEmpty()) {
                        renderState(FavoriteState.Empty)
                    } else {
                        renderState(FavoriteState.Content(vacancies))
                    }
                }
            } catch (e: Exception) {
                Log.e("VACANCIES", e.message.toString())
                renderState(FavoriteState.Error)
            }
        }
    }

    private fun renderState(state: FavoriteState) {
        favoriteState.postValue(state)
    }
}
