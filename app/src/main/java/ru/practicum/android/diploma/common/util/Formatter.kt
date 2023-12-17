package ru.practicum.android.diploma.common.util

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Formatter {

    private const val PHONE_NUMBER_DIGITS_DROP_FIRST_3 = 3
    private const val PHONE_NUMBER_DIGITS_DROP_FIRST_5 = 5
    private const val PHONE_NUMBER_DIGITS_DROP_LAST_2 = 2
    private const val PHONE_NUMBER_DIGITS_DROP_LAST_4 = 4

    private fun getCurrencySymbol(context: Context, currencyCode: String): String {
        val resourceId = when (currencyCode) {
            "AZN" -> R.string.currency_symbol_azn
            "BYR" -> R.string.currency_symbol_byr
            "EUR" -> R.string.currency_symbol_eur
            "GEL" -> R.string.currency_symbol_gel
            "KGS" -> R.string.currency_symbol_kgs
            "KZT" -> R.string.currency_symbol_kzt
            "RUR", "RUB" -> R.string.currency_symbol_rub
            "UAH" -> R.string.currency_symbol_uah
            "USD" -> R.string.currency_symbol_usd
            "UZS" -> R.string.currency_symbol_uzs
            else -> {
                return currencyCode
            }
        }
        return context.getString(resourceId)
    }

    private fun formatNumbersWithSpaces(number: Int): String {
        val formatter = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))
        return formatter.format(number).replace(",", " ")
    }

    fun formatSalary(context: Context, from: Int?, to: Int?, currencyCode: String?): String {
        return when {
            from != null && to != null && currencyCode != null ->
                context.getString(
                    R.string.salary_from_to,
                    formatNumbersWithSpaces(from),
                    formatNumbersWithSpaces(to),
                    getCurrencySymbol(context, currencyCode)
                )

            from != null && currencyCode != null ->
                context.getString(
                    R.string.salary_from,
                    formatNumbersWithSpaces(from),
                    getCurrencySymbol(context, currencyCode)
                )

            to != null && currencyCode != null ->
                context.getString(
                    R.string.salary_to,
                    formatNumbersWithSpaces(to),
                    getCurrencySymbol(context, currencyCode)
                )

            else -> context.getString(R.string.salary_not_found)
        }
    }

    fun formatPhone(country: String, city: String, number: String?): String {
        return String.format(
            Locale.ENGLISH,
            "+%s (%s) %s-%s-%s",
            country,
            city,
            number?.dropLast(PHONE_NUMBER_DIGITS_DROP_LAST_4),
            number?.drop(PHONE_NUMBER_DIGITS_DROP_FIRST_3)?.dropLast(PHONE_NUMBER_DIGITS_DROP_LAST_2),
            number?.drop(PHONE_NUMBER_DIGITS_DROP_FIRST_5)
        )
    }

}
