package com.rkm.tasky.network.model.response

import com.google.gson.annotations.SerializedName
import com.rkm.tasky.network.model.dto.LoginDTO
import com.rkm.tasky.util.storage.model.AuthInfo

data class LoginUserResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("accessTokenExpirationTimestamp")
    val accessTokenExpirationTimestamp: Long
)

fun LoginUserResponse.asLoginDTO(): LoginDTO {
    return LoginDTO(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        fullName = this.fullName,
        userId = this.userId,
        accessTokenExpirationTimestamp = this.accessTokenExpirationTimestamp
    )
}
