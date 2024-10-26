package com.rkm.tasky.network.model.dto

import com.rkm.tasky.util.storage.model.SessionInfo

data class LoginDTO(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)

fun LoginDTO.asSessionInfo(): SessionInfo {
    return SessionInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        fullName = this.fullName,
        userId = this.userId,
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}