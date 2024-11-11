package com.rkm.tasky.network.model.response

import com.google.gson.annotations.SerializedName

data class LoginUserDTO(
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