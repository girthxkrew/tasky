package com.rkm.tasky.util.validator.implementation

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

class EmailPatternValidatorTest {

    private lateinit var validator: EmailPatternValidator

    @Before
    fun setUp() {
        validator = EmailPatternValidator()
    }

    @Test
    fun `valid email`() {
        val testEmail = "user@gmail.com"
        assertTrue(validator.validate(testEmail))
    }

    @Test
    fun `invalid email`() {
        val testEmail = "user"
        assertFalse(validator.validate(testEmail))
    }

    @Test
    fun `empty email`() {
        val testEmail = ""
        assertFalse(validator.validate(testEmail))
    }
}