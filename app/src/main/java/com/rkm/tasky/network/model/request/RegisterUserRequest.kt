package com.rkm.tasky.network.model.request

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)