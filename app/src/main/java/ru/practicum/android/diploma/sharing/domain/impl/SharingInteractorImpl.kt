package ru.practicum.android.diploma.sharing.domain.impl

import ru.practicum.android.diploma.common.util.Formatter
import ru.practicum.android.diploma.sharing.domain.ExternalNavigator
import ru.practicum.android.diploma.sharing.domain.SharingInteractor
import ru.practicum.android.diploma.vacancy.domain.models.Phone

class SharingInteractorImpl(
    val externalNavigator: ExternalNavigator
) : SharingInteractor {
    override fun sendEmail(email: String, subject: String) {
        externalNavigator.sendEmail(email, subject)
    }

    override fun makeCall(phone: Phone) {
        val phoneString = Formatter.formatPhone(phone.country, phone.city, phone.number)
        externalNavigator.makeCall(phoneString)
    }

    override fun shareInMessenger(url: String) {
        externalNavigator.shareInMessenger(url)
    }
}
