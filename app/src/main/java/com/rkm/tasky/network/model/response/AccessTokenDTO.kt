package com.rkm.tasky.network.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("expirationTimeStamp")
    val expirationTimestamp: Long
)


