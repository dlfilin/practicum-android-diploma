package ru.practicum.android.diploma.favorites.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.favorites.data.mapper.FavoriteMapper
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.favorites.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val favoriteMapper: FavoriteMapper
) : FavoriteRepository {
    override  fun getAll(): Flow<List<Favorite>> {
        return appDataBase.favoriteDao().getFavoriteVacancies().map { favorites ->
            favorites.map { favoriteMapper.map(it) }
        }
    }

    override suspend fun addFavoriteVacancy(favorite: Favorite) {
        appDataBase.favoriteDao().addFavoriteVacancy(favoriteMapper.map(favorite))
    }

    override suspend fun deleteFavoriteVacancy(favorite: Favorite) {
        appDataBase.favoriteDao().deleteFavoriteVacancy(favoriteMapper.map(favorite))
    }
}
