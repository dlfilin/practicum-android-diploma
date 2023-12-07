package ru.practicum.android.diploma.filter.ui.adapters

import ru.practicum.android.diploma.filter.domain.models.Area

fun interface ItemClickListener {
    fun onItemListener(item: Area)
}
