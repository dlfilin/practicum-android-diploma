package ru.practicum.android.diploma.favorites.data.db.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.data.db.entity.FavoriteEntity

@Dao
interface FavoritesDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addFavoriteVacancies(favorite: FavoriteEntity)

//    @Query("SELECT * FROM favorites_vacancies")
//    suspend fun getFavoriteVacancies() : Flow<List<FavoriteEntity>>

}
