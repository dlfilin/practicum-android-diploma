package ru.practicum.android.diploma.common.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.filter.presentation.CountryViewModel
import ru.practicum.android.diploma.filter.presentation.FilterViewModel
import ru.practicum.android.diploma.filter.presentation.IndustryViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.SimilarVacanciesViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(searchInteractor = get())
    }

    viewModel { (vacancyId: String) ->
        VacancyViewModel(
            vacancyId = vacancyId,
            vacancyInteractor = get(),
            sharingInteractor = get(),
            favoriteInteractor = get()
        )
    }

    viewModel { (vacancyId: String) ->
        SimilarVacanciesViewModel(vacancyId = vacancyId, searchInteractor = get())
    }

    viewModel {
        FavoritesViewModel(favoriteInteractor = get())
    }

    viewModel {
        FilterViewModel(interactor = get())
    }

    viewModel {
        IndustryViewModel(interactor = get())
    }

    viewModel {
        CountryViewModel(interactor = get())
    }
}
