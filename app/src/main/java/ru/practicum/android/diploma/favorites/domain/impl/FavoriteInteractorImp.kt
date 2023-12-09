package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.mapper.VacancyMapper
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.favorites.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class FavoriteInteractorImp(
    private val favoriteRepository: FavoriteRepository,
    private val vacancyMapper: VacancyMapper
) : FavoriteInteractor {
    override fun getAll(): Flow<List<Favorite>> {
        return favoriteRepository.getAll()
    }

    override suspend fun addFavoriteVacancy(vacancy: Vacancy) {
        val favorite = vacancyMapper.mapToFavorite(vacancy)
        favoriteRepository.addFavoriteVacancy(favorite)
    }

    override suspend fun deleteFavoriteVacancy(vacancy: Vacancy) {
        val favorite = vacancyMapper.mapToFavorite(vacancy)
        favoriteRepository.deleteFavoriteVacancy(favorite)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return favoriteRepository.isFavorite(id)
    }
}
