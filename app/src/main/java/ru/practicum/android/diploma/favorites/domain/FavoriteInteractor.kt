package ru.practicum.android.diploma.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

interface FavoriteInteractor {
    fun getAll(): Flow<List<Favorite>>
    suspend fun addFavoriteVacancy(vacancy: Vacancy)
    suspend fun deleteFavoriteVacancy(vacancy: Vacancy)
    suspend fun isFavorite(id: String): Boolean
}
