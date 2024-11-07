package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.Validator
import javax.inject.Inject

class NamePatternValidator @Inject constructor(): Validator {
    private val namePattern = "^[\\p{L}\\s-]{4,50}$".toRegex()
    override fun validate(input: String): Boolean {
        return input.isNotEmpty() && namePattern.matches(input)
    }
}