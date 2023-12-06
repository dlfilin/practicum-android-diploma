package ru.practicum.android.diploma.favorites.presentation

sealed interface FavoriteState {
    data object Empty: FavoriteState
    data class Content(val vacancies: List<Int>): FavoriteState
}
