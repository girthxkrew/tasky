package com.rkm.tasky.util.storage.implementation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rkm.tasky.testUtils.fake.FakeAndroidKeyStore
import com.rkm.tasky.util.security.implementation.DataStoreEncryptor
import com.rkm.tasky.util.storage.model.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

private const val TEST_DATASTORE_NAME: String = "test_datastore"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SessionStorageImplTest {

    init {
        FakeAndroidKeyStore.setup
    }

    private val context: Context = RuntimeEnvironment.getApplication()
    private val dispatcher = StandardTestDispatcher()
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {context.preferencesDataStoreFile(TEST_DATASTORE_NAME)}
    )
    private val encryptor = DataStoreEncryptor()
    private val sessionStorage = SessionStorageImpl(dataStore, encryptor)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `session is empty`() = runTest(dispatcher) {
        val result = sessionStorage.getSession()
        advanceUntilIdle()
        assertNull(result)
    }

    @Test
    fun `store auth info and retrieve auth info`() = runTest(dispatcher) {
        val authInfo = AuthInfo(
            accessToken = "123",
            refreshToken = "123",
            userId = "1",
            fullName = "Bob Smith",
            email = "email@email.com"
        )
        sessionStorage.setSession(authInfo)
        runCurrent()
        val result = sessionStorage.getSession()
        runCurrent()
        assertTrue(authInfo == result)
    }

    @Test
    fun `store auth info, retrieve auth info, clear auth info`() = runTest(dispatcher) {
        val authInfo = AuthInfo(
            accessToken = "123",
            refreshToken = "123",
            userId = "1",
            fullName = "Bob Smith",
            email = "email@email.com"
        )
        sessionStorage.setSession(authInfo)
        runCurrent()
        val result = sessionStorage.getSession()
        runCurrent()
        assertTrue(authInfo == result)
        sessionStorage.clearSession()
        runCurrent()
        val result2 = sessionStorage.getSession()
        assertNull(result2)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}