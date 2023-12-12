package ru.practicum.android.diploma.common.data.network

import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.util.NetworkResult

interface NetworkClient {
    suspend fun doRequest(dto: Any): NetworkResult<Response>
}
