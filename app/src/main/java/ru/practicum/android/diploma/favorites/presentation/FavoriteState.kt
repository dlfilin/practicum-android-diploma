package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

sealed interface FavoriteState {
    data class Content(val vacancies: List<Vacancy>) : FavoriteState
    object Empty : FavoriteState
    object Error : FavoriteState
    object Loading : FavoriteState
}
