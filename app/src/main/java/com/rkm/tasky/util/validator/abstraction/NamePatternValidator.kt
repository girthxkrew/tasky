package com.rkm.tasky.util.validator.abstraction

interface NamePatternValidator {
    fun isValidName(name: String): Boolean
}