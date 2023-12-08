package ru.practicum.android.diploma.vacancy.data.mapper

import ru.practicum.android.diploma.vacancy.data.dto.ContactsDto
import ru.practicum.android.diploma.vacancy.data.dto.KeySkillDto
import ru.practicum.android.diploma.vacancy.data.dto.PhoneDto
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.domain.models.Contacts
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyMapper {
    fun map(vacancyDto: VacancyDetailsResponse): Vacancy {

        return Vacancy(
            id = vacancyDto.id,
            vacancyName = vacancyDto.name,
            area = vacancyDto.area.name,
            experience = vacancyDto.experience?.name,
            employment = vacancyDto.employment?.name,
            schedule = vacancyDto.schedule?.name,
            salaryFrom = vacancyDto.salary?.from,
            salaryTo = vacancyDto.salary?.to,
            salaryCurrency = vacancyDto.salary?.currency,
            employerName = vacancyDto.employer.name,
            employerLogoUrl = vacancyDto.employer.logoUrls?.logoUrl,
            address = vacancyDto.address?.raw,
            description = vacancyDto.description,
            keySkills = transformKeySkills(vacancyDto.keySkills),
            contacts = mapContacts(vacancyDto.contacts)
        )
    }

    private fun transformKeySkills(keySkills: List<KeySkillDto>?): List<String> {
        return keySkills?.map { it.name } ?: emptyList()
    }

    private fun mapContacts(contactsDto: ContactsDto?): Contacts? {
        return contactsDto?.let {
            Contacts(
                email = it.email,
                name = it.name,
                phones = it.phones?.map { phone ->
                    mapPhone(phone)
                }
            )
        }
    }

    private fun mapPhone(phoneDto: PhoneDto?): Phone? {
        return phoneDto?.let {
            Phone(
                comment = it.comment,
                city = it.city,
                country = it.country,
                number = it.number
            )
        }
    }
}
