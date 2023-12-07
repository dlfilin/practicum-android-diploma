package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.repository.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), convertor = get())
    }
}
