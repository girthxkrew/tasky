package com.rkm.tasky.network.interceptor

import com.rkm.tasky.network.authentication.implementation.AuthenticationManagerImpl
import com.rkm.tasky.network.datasource.TaskyRemoteDataSource
import com.rkm.tasky.network.repository.implementation.AuthenticationRepositoryImpl
import com.rkm.tasky.testUtils.fake.FakeAndroidKeyStore
import com.rkm.tasky.util.storage.implementation.SessionStorageImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TaskyInterceptorTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var server: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var dataSource: TaskyRemoteDataSource
    private lateinit var repository: AuthenticationRepositoryImpl
    private lateinit var sessionStorage: SessionStorageImpl
    private lateinit var manager: AuthenticationManagerImpl

    @Before
    fun setUp() {
        FakeAndroidKeyStore.setup
        Dispatchers.setMain(dispatcher)
        server.start(8080)
        //okHttpClient = OkHttpClient.Builder().addInterceptor()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}