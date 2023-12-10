package ru.practicum.android.diploma.common.data.storage

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto

interface FilterStorage {
    fun saveFilterParameters(filterParam: FilterParametersDto)
    fun getFilterParameters(): FilterParametersDto
}
