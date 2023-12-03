package ru.practicum.android.diploma.common.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomActions {

    fun getCustomActions(): List<CustomAction>

}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)
