package com.rkm.tasky.util.security.implementation

import com.rkm.tasky.testUtils.fake.FakeAndroidKeyStore
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataStoreEncryptorTest {

    private lateinit var encryptor: DataStoreEncryptor


    @Before
    fun setUp() {
        FakeAndroidKeyStore.setup
        encryptor = DataStoreEncryptor()
    }

    @Test
    fun `encryption + decryption test success`() {

        val text = "FakePassword123"
        val encryptedResult = encryptor.encrypt(text)
        val decryptedResult = encryptor.decrypt(encryptedResult)
        Assert.assertTrue(text == decryptedResult)
    }

    @Test
    fun `encryption + decryption test fail`() {
        val text = "FakePassword123"
        val encryptedResult = encryptor.encrypt(text)
        val decryptedResult = encryptor.decrypt(encryptedResult)
        Assert.assertFalse("FakePassword1234" == decryptedResult)
    }
}