package com.rkm.tasky.network.model.response

import android.adservices.adid.AdId
import com.rkm.tasky.util.storage.model.AuthInfo

data class LoginUserResponse(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)

fun LoginUserResponse.asAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken =  this.accessToken,
        refreshToken = this.refreshToken,
        fullName = this.fullName,
        userId = this.userId,
        email = "",
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}
