package ru.practicum.android.diploma.common.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto

class SharedPrefsFilterStorage(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : FilterStorage {
    override fun saveFilterParameters(filterParam: FilterParametersDto) {
        writeHistoryToJson(filterParam)
    }

    override fun getFilterParameters(): FilterParametersDto {
        return readHistoryFromJson()
    }

    private fun readHistoryFromJson(): FilterParametersDto {
        val json =
            sharedPrefs.getString(
                FILTER_PARAMETERS_KEY, null
            ) ?: return FilterParametersDto(null, null, null, false)
        return gson.fromJson(json, FilterParametersDto::class.java)
    }

    private fun writeHistoryToJson(filterParam: FilterParametersDto) {
        val json = gson.toJson(filterParam)
        sharedPrefs.edit()
            .putString(FILTER_PARAMETERS_KEY, json)
            .apply()
    }

    companion object {
        const val SEARCH_FILTER_PREFERENCES = "search_filter_preferences"
        const val FILTER_PARAMETERS_KEY = "filter_parameters_key"
    }
}
