package ru.practicum.android.diploma.sharing.domain

interface ExternalNavigator {
    fun sendEmail(email: String, subject: String)
    fun makeCall(phone: String)
    fun shareInMessenger(url: String)
}
