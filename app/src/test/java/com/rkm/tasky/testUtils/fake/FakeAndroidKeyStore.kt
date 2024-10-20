package com.rkm.tasky.testUtils.fake

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidAlgorithmParameterException
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreSpi
import java.security.PrivateKey
import java.security.Provider
import java.security.SecureRandom
import java.security.Security
import java.security.cert.Certificate
import java.security.spec.AlgorithmParameterSpec
import java.util.Collections
import java.util.Date
import java.util.Enumeration
import javax.crypto.KeyGenerator
import javax.crypto.KeyGeneratorSpi
import javax.crypto.SecretKey

/*
 * For testing EncryptedSharedPreferences Source: https://proandroiddev.com/testing-jetpack-security-with-robolectric-9f9cf2aa4f61
 * For testing AndroidKeyStore implementation source: https://medium.com/@wujingwe/write-unit-test-which-has-androidkeystore-dependency-f12181ae6311
 */

object FakeAndroidKeyStore {

    val setup by lazy {
        Security.addProvider(object : Provider("AndroidKeyStore", 1.0, "Fake AndroidKeyStore provider") {
            init {
                put("KeyStore.AndroidKeyStore", FakeKeyStore::class.java.name)
                put("KeyGenerator.AES", FakeAesKeyGenerator::class.java.name)
            }
        })
    }
}

@Suppress("unused")
class FakeAesKeyGenerator : KeyGeneratorSpi() {

    private val wrapped = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private var spec: KeyGenParameterSpec ?= null

    override fun engineInit(p0: SecureRandom?) {
        throw UnsupportedOperationException(
            "Cannot initialize without a ${KeyGenParameterSpec::class.java.name} parameter"
        )
    }

    override fun engineInit(p0: AlgorithmParameterSpec?, p1: SecureRandom?) {
        if(p0 == null || p0 !is KeyGenParameterSpec) {
            throw InvalidAlgorithmParameterException(
                "Cannot initialize without a ${KeyGenParameterSpec::class.java.name} parameter"
            )
        }
        spec = p0
    }

    override fun engineInit(p0: Int, p1: SecureRandom?) {
        throw UnsupportedOperationException(
            "Cannot initialize without a ${KeyGenParameterSpec::class.java.name} parameter"
        )
    }

    override fun engineGenerateKey(): SecretKey {
        val spec = this.spec ?: throw IllegalStateException("Not Initialized")

        val secretKey = wrapped.generateKey()
        keyStore.setKeyEntry(
            spec.keystoreAlias,
            secretKey,
            null,
            null
        )
        return secretKey
    }
}

@Suppress("unused")
class FakeKeyStore : KeyStoreSpi() {

    companion object {
        private val keys = mutableMapOf<String, Key>()
        private val certs = mutableMapOf<String, Certificate>()
    }

    override fun engineGetEntry(
        alias: String?,
        protParam: KeyStore.ProtectionParameter?
    ): KeyStore.Entry {
        alias ?: throw NullPointerException("alias == null")

        val key = keys[alias]
        if (key != null) {
            return when (key) {
                is SecretKey -> KeyStore.SecretKeyEntry(key)
                is PrivateKey -> KeyStore.PrivateKeyEntry(key, null)
                else -> throw UnsupportedOperationException("Unsupported key type: $key")
            }
        }
        val cert = certs[alias]
        if (cert != null) {
            return KeyStore.TrustedCertificateEntry(cert)
        }
        throw UnsupportedOperationException("No alias found in keys or certs, alias=$alias")
    }

    override fun engineGetKey(p0: String?, p1: CharArray?): Key? {
        p0 ?: throw NullPointerException("p0 == null")
        return keys[p0]
    }

    override fun engineGetCertificateChain(p0: String?): Array<Certificate> {
        p0 ?: throw NullPointerException("p0 == null")
        val cert = certs[p0] ?: return arrayOf()
        return arrayOf(cert)
    }

    override fun engineGetCertificate(p0: String?): Certificate {
        p0 ?: throw NullPointerException("p0 == null")
        return certs.getValue(p0)
    }

    override fun engineGetCreationDate(p0: String?): Date {
        return Date()
    }

    override fun engineSetKeyEntry(
        p0: String?,
        p1: Key?,
        p2: CharArray?,
        p3: Array<out Certificate>?
    ) {
        p0 ?: throw NullPointerException("p0 == null")
        p1 ?: throw NullPointerException("p1 == null")

        keys[p0] = p1
    }

    override fun engineSetKeyEntry(p0: String?, p1: ByteArray?, p2: Array<out Certificate>?) {
        throw UnsupportedOperationException(
            "Operation not supported because key encoding is unknown"
        )
    }

    override fun engineSetCertificateEntry(p0: String?, p1: Certificate?) {
        p0 ?: throw NullPointerException("p0 == null")
        p1 ?: throw NullPointerException("p1 == null")

        certs[p0] = p1
    }

    override fun engineDeleteEntry(p0: String?) {
        p0 ?: throw NullPointerException("p0 == null")
        keys.remove(p0)
        certs.remove(p0)
    }

    override fun engineAliases(): Enumeration<String> {
        val uniqueAlias = mutableSetOf<String>().apply {
            addAll(keys.keys)
            addAll(certs.keys)
        }
        return Collections.enumeration(uniqueAlias)
    }

    override fun engineContainsAlias(p0: String?): Boolean {
        p0 ?: throw NullPointerException("p0 == null")
        return keys.containsKey(p0) || certs.containsKey(p0)
    }

    override fun engineSize(): Int {
        val uniqueAlias = mutableSetOf<String>().apply {
            addAll(keys.keys)
            addAll(certs.keys)
        }
        return uniqueAlias.size
    }

    override fun engineIsKeyEntry(p0: String?): Boolean {
        p0 ?: throw NullPointerException("p0 == null")
        return keys.containsKey(p0)
    }

    override fun engineIsCertificateEntry(p0: String?): Boolean {
        p0 ?: throw NullPointerException("p0 == null")
        return certs.containsKey(p0)
    }

    override fun engineGetCertificateAlias(p0: Certificate?): String? {
        p0 ?: throw NullPointerException("p0 == null")
        for(entry in certs.entries) {
            if(entry.value == p0) {
                return entry.key
            }
        }

        return null
    }

    override fun engineStore(p0: OutputStream?, p1: CharArray?) {
        throw UnsupportedOperationException("Can not serialize AndroidKeyStore to OutputStream")
    }

    override fun engineLoad(p0: InputStream?, p1: CharArray?) {
        if (p0 != null) {
            throw IllegalArgumentException("InputStream not supported")
        }
        if (p1 != null) {
            throw IllegalArgumentException("password not supported")
        }
    }
}