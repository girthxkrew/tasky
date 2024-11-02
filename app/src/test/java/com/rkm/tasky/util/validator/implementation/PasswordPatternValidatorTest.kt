package com.rkm.tasky.util.validator.implementation

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class PasswordPatternValidatorTest {

    private lateinit var passwordValidator: PasswordPatternValidator

    @Before
    fun setUp() {
        passwordValidator = PasswordPatternValidator()
    }

    @Test
    fun `valid password with lowercase, uppercase letters, 1 digit, and length of 9 characters`() {
        val password = "Password1"
        assertTrue(passwordValidator.validate(password))
    }

    @Test
    fun `invalid password with lowercase letters, 1 digit, and length of 9 characters`() {
        val password = "password1"
        assertFalse(passwordValidator.validate(password))
    }

    @Test
    fun `invalid password with uppercase letters, 1 digit, and length of 9 characters`() {
        val password = "PASSWORD1"
        assertFalse(passwordValidator.validate(password))
    }

    @Test
    fun `invalid password with lowercase, uppercase letters, 0 digits, and length of 9 characters`() {
        val password = "Passwordd"
        assertFalse(passwordValidator.validate(password))
    }

    @Test
    fun `invalid password with lowercase, uppercase letters, 1 digits, and length of 8 characters`() {
        val password = "Passw0rd"
        assertFalse(passwordValidator.validate(password))
    }

    @Test
    fun `invalid password because empty`() {
        val password = ""
        assertFalse(passwordValidator.validate(password))
    }
}