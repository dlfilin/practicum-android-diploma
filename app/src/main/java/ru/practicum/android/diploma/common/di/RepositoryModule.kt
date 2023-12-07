package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data .mapper.FavoriteMapper
import ru.practicum.android.diploma.favorites.data .repository.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.repository.FavoriteRepository

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), convertor = get())
    }
    factory { FavoriteMapper() }
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }
}
