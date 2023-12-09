package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.mapper.FavoriteMapper
import ru.practicum.android.diploma.favorites.data.repository.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.repository.FavoriteRepository
import ru.practicum.android.diploma.search.data.repository.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancy.data.mapper.VacancyMapper
import ru.practicum.android.diploma.vacancy.data.repository.ExternalNavigatorImpl
import ru.practicum.android.diploma.vacancy.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), converter = get())
    }
    factory { FavoriteMapper() }
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(appDataBase = get(), favoriteMapper = get())
    }
    factory { VacancyMapper() }
    single<VacancyRepository> {
        VacancyRepositoryImpl(networkClient = get(), vacancyMapper = get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }
}
