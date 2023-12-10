package ru.practicum.android.diploma.favorites.data.mapper

import ru.practicum.android.diploma.favorites.data.db.entity.FavoriteEntity
import ru.practicum.android.diploma.favorites.domain.models.Favorite

class FavoriteMapper {
    fun map(favorite: FavoriteEntity): Favorite {
        return Favorite(
            id = favorite.id,
            nameVacancies = favorite.nameVacancies,
            logoUrl = favorite.logoUrl,
            salary = favorite.salary,
            nameCompany = favorite.nameCompany,
            city = favorite.city,
            experience = favorite.experience,
            schedule = favorite.schedule,
            employment = favorite.employment,
            description = favorite.description,
            keySkills = favorite.keySkills,
            contactPerson = favorite.contactPerson,
            contactEmail = favorite.contactEmail,
            contactPhone = favorite.contactPhone,
            comment = favorite.comment,
            addTime = favorite.addTime,
            alternateUrl = favorite.vacancyUrl
        )
    }

    fun map(favorite: Favorite): FavoriteEntity {
        return FavoriteEntity(
            id = favorite.id,
            nameVacancies = favorite.nameVacancies,
            logoUrl = favorite.logoUrl,
            salary = favorite.salary,
            nameCompany = favorite.nameCompany,
            city = favorite.city,
            experience = favorite.experience,
            schedule = favorite.schedule,
            employment = favorite.employment,
            description = favorite.description,
            keySkills = favorite.keySkills,
            contactPerson = favorite.contactPerson,
            contactEmail = favorite.contactEmail,
            contactPhone = favorite.contactPhone,
            comment = favorite.comment,
            addTime = favorite.addTime,
            vacancyUrl = favorite.alternateUrl
        )
    }
}
