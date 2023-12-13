package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.common.data.db.dao.FavoritesDao
import ru.practicum.android.diploma.favorites.data.db.entity.FavoriteEntity

@Database(
    version = 1,
    entities = [FavoriteEntity::class],
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoritesDao
}
