package ru.practicum.android.diploma.favorites.domain.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.favorites.domain.models.Favorite
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy
import java.util.Locale

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

    fun mapToVacancy(favorite: Favorite): Vacancy {
        return Vacancy(
            id = favorite.id,
            vacancyName = favorite.nameVacancies,
            employerName = favorite.nameCompany,
            employerLogoUrl = favorite.logoUrl,
            salaryFrom = getSalaryFrom(favorite.salary!!),
            salaryTo = getSalaryTo(favorite.salary),
            salaryCurrency = transformSalaryCurrencyFromString(favorite.salary),
            area = convertFromGsonToArea(favorite.city),
            experience = favorite.experience,
            schedule = favorite.schedule,
            employment = favorite.employment,
            description = favorite.description!!,
            keySkills = convertFromGsonKeySkills(favorite.keySkills),
            contacts = transformContactsFromFavorites(
                favorite.contactPerson,
                favorite.contactEmail,
                favorite.contactPhone
            ),
            alternateUrl = favorite.alternateUrl,
            address = convertFromGsonToAddress(favorite.city)
        )
    }

    private fun getObjectFromGson(city: String): City {
        val itemType = object : TypeToken<City>() {}.type
        return gson.fromJson(city, itemType)
    }

    private fun convertFromGsonToAddress(city: String): String? {
        val cityObject = getObjectFromGson(city)
        if (cityObject.address != null) return cityObject.address
        return null
    }

    private fun convertFromGsonToArea(city: String): String {
        val cityObject = getObjectFromGson(city)
        return cityObject.area
    }

    private fun convertToGsonCity(vacancy: Vacancy): String {
        val city = City(area = vacancy.area, address = vacancy.address)
        return gson.toJson(city)
    }

    private fun convertToGsonPhones(phones: List<Phone?>?): String? {
        if (phones == null) return null
        return gson.toJson(phones)
    }

    private fun convertToGsonKeySkills(skills: List<String>?): String {
        return gson.toJson(skills)
    }

    private fun convertFromGsonKeySkills(skills: String?): List<String>? {
        if (skills != null) {
            val itemType = object : TypeToken<List<String>>() {}.type
            return gson.fromJson<List<String>>(skills, itemType)
        }

        return null
    }

    private fun transformSalaryToString(salaryFrom: Int?, salaryTo: Int?, salaryCurrency: String?): String {
        return String.format(
            Locale.ENGLISH,
            "%d__%d__%s",
            salaryFrom,
            salaryTo,
            salaryCurrency
        )
    }

    private fun getSalaryFrom(salary: String): Int? {
        val salaryArray = salary.split("__").toTypedArray()
        if (salaryArray[0] != "null") return salaryArray[0].toInt()
        return null
    }

    private fun getSalaryTo(salary: String): Int? {
        val salaryArray = salary.split("__").toTypedArray()
        if (salaryArray[1] != "null") return salaryArray[0].toInt()
        return null
    }

    private fun transformSalaryCurrencyFromString(salary: String): String {
        val strs = salary.split("__").toTypedArray()
        return strs[2]
    }

    private fun transformContactsToContactPerson(contacts: Contacts?): String? {
        if (contacts?.name != null) return contacts.name.toString()
        return null
    }

    private fun transformContactsToContactEmail(contacts: Contacts?): String? {
        if (contacts?.email != null) return contacts.email.toString()
        return null
    }

    private fun transformContactsFromFavorites(person: String?, email: String?, phones: String?): Contacts {
        var phoneList = listOf<Phone>()
        if (phones != null && phones != "null") {
            val itemType = object : TypeToken<List<Phone>>() {}.type
            phoneList = gson.fromJson(phones, itemType)
        }

        return Contacts(
            name = person,
            email = email,
            phones = phoneList
        )
    }

    companion object {
        data class City(
            val area: String,
            val address: String?
        )
    }
}
