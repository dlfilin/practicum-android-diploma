package ru.practicum.android.diploma.filter.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo("name")
    val name: String,
)
