package com.rkm.tasky.network.util

import com.rkm.tasky.util.result.Error

sealed interface NetworkError: Error {
    enum class AuthError: Error {
        UNKNOWN,
        NO_INTERNET,
        INVALID_TOKEN,
        UNAUTHORIZED,
        NOT_FOUND,
        FORBIDDEN,
        REGISTER_USER_ERROR,
        LOGIN_USER_ERROR,
        CHECK_AUTHENTICATION_ERROR,
        LOGOUT_ERROR
    }
}