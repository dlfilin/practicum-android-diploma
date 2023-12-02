package ru.practicum.android.diploma.favorites.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorites_vacancies")
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("name_vacancies")
    val nameVacancies: String,
    @ColumnInfo("logo_url")
    val logoUrl: String,
    @ColumnInfo("salary")
    val salary: String,
    @ColumnInfo("name_company")
    val nameCompany: String,
    @ColumnInfo("city")
    val city: String,
    @ColumnInfo("experience")
    val experience: String,
    @ColumnInfo("schedule")
    val schedule: String,
    @ColumnInfo("employment")
    val employment: String,
    @ColumnInfo("responsibilities")
    val responsibilities: String,
    @ColumnInfo("requirements")
    val requirements: String,
    @ColumnInfo("conditions")
    val conditions: String,
    @ColumnInfo("key_skills")
    val keySkills: String?,
    @ColumnInfo("contact_person")
    val contactPerson: String?,
    @ColumnInfo("contact_email")
    val contactEmail: String?,
    @ColumnInfo("contact_phone")
    val contactPhone: String?,
    @ColumnInfo("comment")
    val comment: String?,
)



