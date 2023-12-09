package ru.practicum.android.diploma.favorites.domain.mapper

import com.google.gson.Gson
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyMapper {

    private val gson = Gson()

    fun mapToFavorite(vacancy: Vacancy): Favorite {
        return Favorite(
            id = vacancy.id,
            nameVacancies = vacancy.vacancyName,
            logoUrl = vacancy.employerLogoUrl,
            salary = transformSalaryToString(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency),
            nameCompany = vacancy.employerName,
            city = convertToGsonCity(vacancy),
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            description = vacancy.description,
            keySkills = convertToGsonKeySkills(vacancy.keySkills),
            contactPerson = transformContactsToContactPerson(vacancy.contacts),
            contactEmail = transformContactsToContactEmail(vacancy.contacts),
            contactPhone = convertToGsonPhones(vacancy.contacts?.phones),
            comment = null,
            alternateUrl = vacancy.alternateUrl,
            addTime = System.currentTimeMillis()
        )
    }

    private fun convertToGsonCity(vacancy: Vacancy): String {
        val city = City(area = vacancy.area, address = vacancy.address)
        return gson.toJson(city)
    }

    private fun convertToGsonPhones(phones: List<Phone?>?): String {
        return gson.toJson(phones)
    }

    private fun convertToGsonKeySkills(skills: List<String>?): String {
        return gson.toJson(skills)
    }

    private fun transformSalaryToString(salaryFrom: Int?, salaryTo: Int?, salaryCurrency: String?): String {
        return String.format(
            "%d__%d__%s",
            salaryFrom,
            salaryTo,
            salaryCurrency
        )
    }

    private fun transformContactsToContactPerson(contacts: Contacts?): String {
        return contacts?.name.toString()
    }

    private fun transformContactsToContactEmail(contacts: Contacts?): String {
        return contacts?.email.toString()
    }

    companion object {
        data class City(
            val area: String?,
            val address: String?
        )
    }
}
