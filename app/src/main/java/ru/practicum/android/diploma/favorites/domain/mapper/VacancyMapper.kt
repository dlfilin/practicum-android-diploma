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
            salaryFrom = transformSalaryFromString(favorite.salary!!, "from"),
            salaryTo = transformSalaryFromString(favorite.salary, "to"),
            salaryCurrency = transformSalaryCurrencyFromString(favorite.salary),
            area = convertFromGsonCity(favorite.city, "area"),
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
            address = convertFromGsonCity(favorite.city, "address")
        )
    }

    private fun convertFromGsonCity(city: String?, flag: String = "unknown"): String {
        if (city != null) {
            val itemType = object : TypeToken<City>() {}.type
            val cityObject = gson.fromJson<City>(city, itemType)
            if (flag == "address" && cityObject.address != null) {
                return cityObject.address
            } else if (flag == "area" && cityObject.area != null) {
                return cityObject.area
            }
        }

        return flag
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

    private fun transformSalaryFromString(salary: String, flag: String): Int? {
        val strs = salary.split("__").toTypedArray()
        if (flag == "to" && strs[1] != "null") {
            return strs[1].toInt()
        }
        if (flag == "from" && strs[0] != "null") {
            return strs[0].toInt()
        }
        return null
    }

    private fun transformSalaryCurrencyFromString(salary: String): String {
        val strs = salary.split("__").toTypedArray()
        return strs[2]
    }

    private fun transformContactsToContactPerson(contacts: Contacts?): String {
        return contacts?.name.toString()
    }

    private fun transformContactsToContactEmail(contacts: Contacts?): String {
        return contacts?.email.toString()
    }

    private fun transformContactsFromFavorites(person: String?, email: String?, phones: String?): Contacts {
        var phoneList = listOf<Phone>()
        if (phones != null) {
            val itemType = object : TypeToken<List<Phone>>() {}.type
            phoneList = gson.fromJson<List<Phone>>(phones, itemType)
        }

        return Contacts(
            name = person,
            email = email,
            phones = phoneList
        )
    }

    companion object {
        data class City(
            val area: String?,
            val address: String?
        )
    }
}
