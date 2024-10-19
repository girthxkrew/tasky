package com.rkm.tasky.util.security.abstraction

interface Encryptor {
    fun encrypt(data: String): String
    fun decrypt(data: String): String
}