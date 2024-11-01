package com.rkm.tasky.feature.fakes

import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator

class EmailPatternValidatorFake: EmailPatternValidator {
    override fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}