package com.rkm.tasky.network.authorization.model

data class AuthInfoDTO(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val fullName: String,
    val accessTokenExpirationTimestamp: Long
)
