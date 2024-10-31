package com.rkm.tasky.util.validator.implementation

import com.rkm.tasky.util.validator.abstraction.EmailPatternValidator
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EmailPatternValidatorImplTest {

    private lateinit var validator: EmailPatternValidator

    @Before
    fun setUp() {
        validator = EmailPatternValidatorImpl()
    }

    @Test
    fun isValidEmail() {
        val testEmail = "user@gmail.com"
        assertTrue(validator.isValidEmail(testEmail))
    }

    @Test
    fun notValidEmail() {
        val testEmail = "user"
        assertTrue(!validator.isValidEmail(testEmail))
    }
}