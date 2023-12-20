package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.favorites.domain.FavoriteInteractor
import ru.practicum.android.diploma.sharing.domain.SharingInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyViewModel(
    private val vacancyId: String,
    private val vacancyInteractor: VacancyInteractor,
    private val sharingInteractor: SharingInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private var isFavorite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavorite
    private val vacancyState = MutableLiveData<VacancyScreenState>()
    fun observeVacancyState(): LiveData<VacancyScreenState> = vacancyState
    private val _currentVacancy = MutableLiveData<Vacancy?>(null)
    val currentVacancy = _currentVacancy as LiveData<Vacancy?>

    fun getVacancy(vacancyId: String) {
        vacancyState.postValue(VacancyScreenState.Loading)
        viewModelScope.launch {
            vacancyInteractor.getVacancy(vacancyId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _currentVacancy.value = result.data
                        renderState(VacancyScreenState.Content(result.data!!))
                    }

                    is NetworkResult.Error -> {
                        if (result.errorType == ErrorType.NO_INTERNET) {
                            getVacancyLocal(vacancyId)
                            renderState(VacancyScreenState.Loading)
                        } else {
                            renderState(VacancyScreenState.Error)
                        }
                    }
                }
                val favoriteStatus = isFavorite(vacancyId)
                isFavorite.postValue(favoriteStatus)
            }
        }
    }

    fun shareVacancy(vacancyUrl: String) {
        sharingInteractor.shareInMessenger(vacancyUrl)
    }

    fun makeCall(phone: Phone) {
        sharingInteractor.makeCall(phone)
    }

    fun sendEmail(email: String, subject: String) {
        sharingInteractor.sendEmail(email, subject)
    }

    private fun renderState(state: VacancyScreenState) {
        vacancyState.postValue(state)
    }

    fun toggleFavorite(vacancy: Vacancy) {
        viewModelScope.launch {
            val favoriteStatus = isFavorite(vacancy.id)

            if (favoriteStatus) {
                // delete
                vacancy.let { favoriteInteractor.deleteFavoriteVacancy(it) }
            } else {
                // add
                vacancy.let { favoriteInteractor.addFavoriteVacancy(it) }
            }
            isFavorite.postValue(!favoriteStatus)
        }
    }

    private suspend fun getVacancyLocal(vacancyId: String) {
        viewModelScope.launch {
            val vacancy = favoriteInteractor.getById(vacancyId)
            _currentVacancy.value = vacancy
            if (vacancy != null) {
                renderState(VacancyScreenState.Content(vacancy))
            } else {
                renderState(VacancyScreenState.InternetThrowable)
            }
        }
    }

    private suspend fun isFavorite(vacancyId: String): Boolean {
        return favoriteInteractor.isFavorite(vacancyId)
    }
}
