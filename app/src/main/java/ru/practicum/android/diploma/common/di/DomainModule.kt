package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImp
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractorImpl

val domainModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImp(favoriteRepository = get())
    }
    single<VacancyInteractor> {
        VacancyInteractorImpl(vacancyRepository = get())
    }
}


