package com.rkm.tasky.network.model.dto

import com.rkm.tasky.util.storage.model.AuthInfo

data class LoginDTO(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)

fun LoginDTO.asAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        fullName = this.fullName,
        userId = this.userId,
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}