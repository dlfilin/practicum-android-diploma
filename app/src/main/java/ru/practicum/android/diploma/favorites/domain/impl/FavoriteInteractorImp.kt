package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.favorites.domain.repository.FavoriteRepository

class FavoriteInteractorImp(
    private val favoriteRepository: FavoriteRepository
) : FavoriteInteractor {
    override fun getAll(): Flow<List<Favorite>> {
        return favoriteRepository.getAll()
    }

    override suspend fun addFavoriteVacancy(favorite: Favorite) {
        favoriteRepository.addFavoriteVacancy(favorite)
    }

    override suspend fun deleteFavoriteVacancy(favorite: Favorite) {
        favoriteRepository.deleteFavoriteVacancy(favorite)
    }
}
