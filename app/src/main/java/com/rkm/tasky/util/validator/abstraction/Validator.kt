package com.rkm.tasky.util.validator.abstraction

interface Validator {
    fun validate(input: String): Boolean
}