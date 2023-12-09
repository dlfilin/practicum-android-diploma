package ru.practicum.android.diploma.favorites.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.models.Favorite

interface FavoriteRepository {
    fun getAll(): Flow<List<Favorite>>
    suspend fun addFavoriteVacancy(favorite: Favorite)
    suspend fun deleteFavoriteVacancy(favorite: Favorite)
    suspend fun isFavorite(id: String): Boolean
}
