package ru.practicum.android.diploma.common.util

sealed class Result<T>(val data: T? = null, val errorType: ErrorType? = null) {

    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(type: ErrorType, data: T? = null) : Result<T>(data, type)
}

enum class ErrorType {
    NO_INTERNET,
    SERVER_THROWABLE
}
