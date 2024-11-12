package com.rkm.tasky.network.authentication.mapper

import com.rkm.tasky.network.model.response.LoginUserDTO
import com.rkm.tasky.util.storage.model.SessionInfo

fun LoginUserDTO.asSessionInfo(): SessionInfo {
    return SessionInfo(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userId = this.userId,
        fullName = this.fullName,
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}