package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.impl.SearchInteractorImpl


val domainModule = module {
  val domainModule = module {
    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
        
    single<FavoriteInteractor> {
        FavoriteInteractorImp(get())
    }
}
