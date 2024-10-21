package com.rkm.tasky.network.model.response

import android.adservices.adid.AdId

data class LoginUserResponse(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)
