package ru.practicum.android.diploma.common.data.network

import ru.practicum.android.diploma.common.data.network.dto.Request
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.util.NetworkResult

interface NetworkClient {
    suspend fun doRequest(dto: Request): NetworkResult<Response>
}
