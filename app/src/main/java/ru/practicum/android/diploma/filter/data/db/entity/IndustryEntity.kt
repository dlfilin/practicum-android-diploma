package ru.practicum.android.diploma.filter.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("industry")
data class IndustryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo("name")
    val name: String,
)
