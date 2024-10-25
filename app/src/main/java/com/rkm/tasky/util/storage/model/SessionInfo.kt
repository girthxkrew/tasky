package com.rkm.tasky.util.storage.model

import com.google.gson.annotations.SerializedName
import com.rkm.tasky.network.authorization.model.AuthInfoDTO

data class SessionInfo (
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("accessTokenExpirationTimestamp")
    val accessTokenExpirationTimestamp: Long
)

fun SessionInfo.asAuthInfoDTO(): AuthInfoDTO {
    return AuthInfoDTO(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userId = this.userId,
        fullName = this.fullName,
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}