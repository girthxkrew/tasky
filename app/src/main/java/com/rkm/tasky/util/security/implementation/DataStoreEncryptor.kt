package com.rkm.tasky.util.security.implementation

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.google.gson.Gson
import com.rkm.tasky.util.security.abstraction.Encryptor
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class DataStoreEncryptor @Inject constructor(): Encryptor{

    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val ALIAS_KEY = "ALIAS"
    private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val KEY_SIZE = 128

    private lateinit var keyStore: KeyStore

    init {
        initKeyStore()
    }


    override fun encrypt(data: String): String {
        val cipher = getCipherForEncryption()
        val encryption = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val cipherObject = CipherWrapper(text = encryption, initializationVector = cipher.iv)
        return Gson().toJson(cipherObject)
    }

    override fun decrypt(data: String): String {
        val cipherWrapper = Gson().fromJson(data, CipherWrapper::class.java)
        val cipher = getCipherForDecryption(cipherWrapper.initializationVector)
        val text = cipher.doFinal(cipherWrapper.text)
        return String(text, Charsets.UTF_8)
    }

    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    private fun getCipherForEncryption(): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    private fun getCipherForDecryption(initializationVector: ByteArray): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey()
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(KEY_SIZE, initializationVector))
        return cipher
    }

    private fun getOrCreateSecretKey(): SecretKey {
        keyStore.getKey(ALIAS_KEY, null)?.let { return it as SecretKey }

        val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                ALIAS_KEY,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(ENCRYPTION_BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setKeySize(KEY_SIZE)
                .build()
        )

        return keyGenerator.generateKey()
    }
}

private data class CipherWrapper(
    val text: ByteArray,
    val initializationVector: ByteArray
)