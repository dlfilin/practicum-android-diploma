package ru.practicum.android.diploma.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.common.data.db.dao.FavoritesDao
import ru.practicum.android.diploma.common.data.db.dao.FilterDao
import ru.practicum.android.diploma.favorites.data.db.entity.FavoriteEntity
import ru.practicum.android.diploma.filter.data.db.entity.AreaEntity
import ru.practicum.android.diploma.filter.data.db.entity.CountryEntity
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity

@Database(
    version = 1,
    entities = [FavoriteEntity::class, IndustryEntity::class, AreaEntity::class, CountryEntity::class],
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun favoriteDao(): FavoritesDao
    abstract fun filterDao(): FilterDao
}
