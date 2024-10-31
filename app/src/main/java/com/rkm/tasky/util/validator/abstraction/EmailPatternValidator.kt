package com.rkm.tasky.util.validator.abstraction

interface EmailPatternValidator {
    fun isValidEmail(email: String): Boolean
}