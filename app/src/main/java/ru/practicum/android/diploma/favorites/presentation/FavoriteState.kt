package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

sealed interface FavoriteState {
    data class Content(val vacancies: List<Vacancy>) : FavoriteState
    data object Empty : FavoriteState
    data object Error : FavoriteState
    data object Default : FavoriteState
}
