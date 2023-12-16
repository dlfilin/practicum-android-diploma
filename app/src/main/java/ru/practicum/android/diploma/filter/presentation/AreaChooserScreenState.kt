package ru.practicum.android.diploma.filter.presentation

import ru.practicum.android.diploma.filter.domain.models.Area

sealed interface AreaChooserScreenState {
    data class Content(
        val items: List<Area>,
    ) : AreaChooserScreenState

    data object Error : AreaChooserScreenState
}
