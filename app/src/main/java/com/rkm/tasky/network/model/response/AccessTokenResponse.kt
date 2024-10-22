package com.rkm.tasky.network.model.response

import com.google.gson.annotations.SerializedName
import com.rkm.tasky.network.model.dto.AccessTokenDTO

data class AccessTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("expirationTimeStamp")
    val expirationTimestamp: Long
)

fun AccessTokenResponse.asAccessTokenDTO(): AccessTokenDTO {
    return AccessTokenDTO(
        accessToken = this.accessToken,
        expirationTimestamp = this.expirationTimestamp
    )
}
