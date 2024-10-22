package com.rkm.tasky.util.storage.model

import com.google.gson.annotations.SerializedName

data class AuthInfo (
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