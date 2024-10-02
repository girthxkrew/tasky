package com.rkm.network.model.request

data class RegisterUserRequest(
    val fullName: String,
    val email: String,
    val password: String
)