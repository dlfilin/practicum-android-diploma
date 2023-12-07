package ru.practicum.android.diploma.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.models.Favorite

interface FavoriteInteractor {
    fun getAll(): Flow<List<Favorite>>
    suspend fun addFavoriteVacancy(favorite: Favorite)
    suspend fun deleteFavoriteVacancy(favorite: Favorite)
}
