package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImp
import ru.practicum.android.diploma.favorites.domain.mapper.VacancyMapper
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.sharing.domain.SharingInteractor
import ru.practicum.android.diploma.sharing.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractorImpl

val domainModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
    factory {
        VacancyMapper(gson = get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImp(favoriteRepository = get(), vacancyMapper = get())
    }
    single<VacancyInteractor> {
        VacancyInteractorImpl(vacancyRepository = get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }
    single<FilterInteractor> {
        FilterInteractorImpl(repository = get())
    }
}


