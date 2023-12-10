package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.data.db.entity.FavoriteEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteVacancy(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavoriteVacancy(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites_vacancies")
    fun getFavoriteVacancies(): Flow<List<FavoriteEntity>>

    @Query("SELECT COUNT(*) FROM favorites_vacancies WHERE id = :id")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorites_vacancies WHERE id = :id")
    suspend fun getById(id: String): FavoriteEntity?
}
