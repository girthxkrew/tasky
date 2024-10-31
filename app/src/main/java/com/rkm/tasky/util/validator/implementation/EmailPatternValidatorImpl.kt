package com.rkm.tasky.util.validator.implementation

import android.util.Patterns
import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import javax.inject.Inject

class EmailPatternValidatorImpl @Inject constructor(): EmailPatternValidator {
    override fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}