package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.Validator
import javax.inject.Inject

class NamePatternValidator @Inject constructor(): Validator {
    override fun validate(input: String): Boolean {
        val namePattern = "^[A-Za-z\\s-]{4,50}$".toRegex()
        return input.isNotEmpty() && namePattern.matches(input)
    }
}