package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.PasswordPatternValidator
import javax.inject.Inject

class PasswordPatternValidatorImpl @Inject constructor(): PasswordPatternValidator {
    override fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{9,}$".toRegex()
        return passwordPattern.matches(password)
    }
}