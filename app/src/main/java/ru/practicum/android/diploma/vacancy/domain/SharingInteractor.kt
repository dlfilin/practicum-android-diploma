package ru.practicum.android.diploma.vacancy.domain

interface SharingInteractor {
    fun sendEmail(email: String, subject: String)
}
