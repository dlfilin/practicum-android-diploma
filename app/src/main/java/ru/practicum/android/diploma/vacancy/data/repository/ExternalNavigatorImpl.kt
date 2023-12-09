package ru.practicum.android.diploma.vacancy.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun sendEmail(email: String, subject: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(this)
        }
    }

    override fun makeCall(phone: String) {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK

            context.startActivity(this)
        }
    }
}
