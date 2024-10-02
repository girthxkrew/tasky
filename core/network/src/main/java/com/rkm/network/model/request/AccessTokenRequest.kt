package com.rkm.network.model.request

data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String
)
