package ru.practicum.android.diploma.vacancy.domain

interface ExternalNavigator {
    fun sendEmail(email: String, subject: String)
    fun makeCall(phone: String)
    fun shareInMessenger(url: String)
}
