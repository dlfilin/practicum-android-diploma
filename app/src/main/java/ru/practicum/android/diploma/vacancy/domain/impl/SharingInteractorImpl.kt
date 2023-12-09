package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.SharingInteractor
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class SharingInteractorImpl(
    val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun sendEmail(email: String, subject: String) {
        externalNavigator.sendEmail(email, subject)
    }
}
