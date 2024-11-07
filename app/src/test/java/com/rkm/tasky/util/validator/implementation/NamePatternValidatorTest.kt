package com.rkm.tasky.util.validator.implementation

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class NamePatternValidatorTest {

    private lateinit var nameValidator: NamePatternValidator

    @Before
    fun setUp() {
        nameValidator = NamePatternValidator()
    }

    @Test
    fun `invalid name less than 4 characters`() {
        val name = "Bob"
        assertFalse(nameValidator.validate(name))
    }

    @Test
    fun `valid name equal to 4 characters`() {
        val name = "Rene"
        assertTrue(nameValidator.validate(name))
    }

    @Test
    fun `valid name in between 4 and 50`() {
        val name = "Bob Smith"
        assertTrue(nameValidator.validate(name))
    }

    @Test
    fun `invalid name more than 50 characters`() {
        val name = StringBuilder()
        repeat(51) {
            name.append("r")
        }
        assertFalse(nameValidator.validate(name.toString()))
    }

    @Test
    fun `invalid name with numbers`() {
        val name = StringBuilder()
        repeat(25) {
            name.append("r1")
        }
        assertFalse(nameValidator.validate(name.toString()))
    }

    @Test
    fun `empty name invalid`() {
        val name = ""
        assertFalse(nameValidator.validate(name))
    }

    @Test
    fun `internation name`() {
        val name = "José Álvarez"
        assertTrue(nameValidator.validate(name))
    }
}