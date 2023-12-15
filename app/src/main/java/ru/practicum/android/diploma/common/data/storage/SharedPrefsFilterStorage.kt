package ru.practicum.android.diploma.common.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto

class SharedPrefsFilterStorage(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : FilterStorage {
    override fun saveFilterParameters(filterParam: FilterParametersDto) {
        val json = gson.toJson(filterParam)
        sharedPrefs.edit()
            .putString(FILTER_PARAMETERS_KEY, json)
            .apply()
    }

    override fun getFilterParameters(): FilterParametersDto {
        val json =
            sharedPrefs.getString(
                FILTER_PARAMETERS_KEY, null
            ) ?: return FilterParametersDto(
                area = null,
                country = null,
                industry = null,
                salary = null,
                onlyWithSalary = false
            )
        return gson.fromJson(json, FilterParametersDto::class.java)
    }

    override fun saveEditFilterParameters(editFilterParam: FilterParametersDto) {
        val json = gson.toJson(editFilterParam)
        sharedPrefs.edit()
            .putString(EDIT_FILTER_PARAMETERS_KEY, json)
            .apply()
    }

    override fun getEditFilterParameters(): FilterParametersDto {
        val json =
            sharedPrefs.getString(
                EDIT_FILTER_PARAMETERS_KEY, null
            ) ?: return FilterParametersDto(
                area = null,
                country = null,
                industry = null,
                salary = null,
                onlyWithSalary = false
            )
        return gson.fromJson(json, FilterParametersDto::class.java)
    }

    override fun isFilterActive(): Boolean {
        val filter = getFilterParameters()
        return filter.isNotEmpty
    }

    companion object {
        const val SEARCH_FILTER_PREFERENCES = "search_filter_preferences"
        const val FILTER_PARAMETERS_KEY = "filter_parameters_key"
        const val EDIT_FILTER_PARAMETERS_KEY = "edit_filter_parameters_key"
    }
}
