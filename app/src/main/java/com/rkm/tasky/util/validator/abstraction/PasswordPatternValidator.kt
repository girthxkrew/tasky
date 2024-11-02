package com.rkm.tasky.util.validator.abstraction

interface PasswordPatternValidator {
    fun isValidPassword(password: String): Boolean
}