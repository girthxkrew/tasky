package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.NamePatternValidator
import javax.inject.Inject

class NamePatternValidatorImpl @Inject constructor(): NamePatternValidator {
    private val minLength = 4
    private val maxLength = 50
    override fun isValidName(name: String): Boolean {
        return name.length in minLength..maxLength
    }
}