package com.rkm.tasky.network.model.dto


data class AccessTokenDTO(
    val accessToken: String,
    val expirationTimestamp: Long
)
