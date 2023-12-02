package ru.practicum.android.diploma.common.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.db.AppDataBase

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "dataBase.db").build()
    }

}
