package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.favorites.domain.models.Favorite

sealed interface FavoriteState {
    data class Content(val vacancies: List<Favorite>) : FavoriteState
    object Empty : FavoriteState
    object Error : FavoriteState
}
