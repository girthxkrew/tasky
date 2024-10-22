package com.rkm.tasky.network.model.request

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: String
)
