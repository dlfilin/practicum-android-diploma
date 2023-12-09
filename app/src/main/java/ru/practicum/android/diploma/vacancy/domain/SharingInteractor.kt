package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.vacancy.domain.models.Phone

interface SharingInteractor {
    fun sendEmail(email: String, subject: String)
    fun makeCall(phone: Phone)
    fun shareInMessenger(url: String)
}
