package com.rkm.tasky.network.model.request

data class LoginUserRequest(
    val email: String,
    val password: String
)
