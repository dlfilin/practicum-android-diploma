package ru.practicum.android.diploma.common.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImp


val domainModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImp(get())
    }
}
