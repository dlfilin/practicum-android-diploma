package ru.practicum.android.diploma.common.util

sealed class NetworkResult<T>(val data: T? = null, val errorType: ErrorType? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(type: ErrorType, data: T? = null) : NetworkResult<T>(data, type)
}

enum class ErrorType {
    NO_INTERNET,
    NON_200_RESPONSE,
    SERVER_THROWABLE,
    BAD_REQUEST
}
