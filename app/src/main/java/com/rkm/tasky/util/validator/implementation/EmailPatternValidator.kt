package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.Validator
import javax.inject.Inject

class EmailPatternValidator @Inject constructor(): Validator {
    override fun validate(input: String): Boolean {
        val emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()
        return input.isNotEmpty() && emailPattern.matches(input)
    }
}