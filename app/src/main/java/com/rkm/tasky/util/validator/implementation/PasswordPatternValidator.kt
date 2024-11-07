package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.Validator
import javax.inject.Inject

class PasswordPatternValidator @Inject constructor(): Validator {
    private val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{9,}$".toRegex()
    override fun validate(input: String): Boolean {
        return input.isNotEmpty() && passwordPattern.matches(input)
    }
}