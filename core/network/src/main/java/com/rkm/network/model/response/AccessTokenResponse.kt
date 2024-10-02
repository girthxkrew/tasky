package com.rkm.network.model.response

data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)
