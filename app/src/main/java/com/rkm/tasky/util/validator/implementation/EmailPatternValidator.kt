package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.Validator
import javax.inject.Inject

class EmailPatternValidator @Inject constructor(): Validator {
    private val emailPattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    override fun validate(input: String): Boolean {
        return input.isNotEmpty() && emailPattern.matches(input)
    }
}