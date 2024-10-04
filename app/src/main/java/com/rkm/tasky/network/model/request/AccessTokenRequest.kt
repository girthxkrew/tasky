package com.rkm.tasky.network.model.request

data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String
)
