package ru.practicum.android.diploma.common.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(searchInteractor = get())
    }

    viewModel { (vacancyId: String) ->
        VacancyViewModel(vacancyId)
    }

    viewModel { (vacancyId: String) ->
        SimilarVacanciesViewModel(vacancyId, get())
    }

    viewModel {
        FavoritesViewModel(get())
    }
}
