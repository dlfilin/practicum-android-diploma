package ru.practicum.android.diploma.common.data.storage

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto

interface FilterStorage {
    fun saveMainFilterParameters(mainFilterParam: FilterParametersDto)
    fun getMainFilterParameters(): FilterParametersDto
    fun saveEditableFilterParameters(editableFilterParam: FilterParametersDto)
    fun getEditableFilterParameters(): FilterParametersDto
    fun isFilterActive(): Boolean
}
