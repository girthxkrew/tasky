package com.rkm.tasky.repository.implementation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.network.model.dto.asReminderDTO
import com.rkm.tasky.repository.mapper.asReminder
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.utils.json.reminderResponseToPojo
import com.rkm.tasky.utils.json.reminderResponseToString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class TaskyReminderRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val id = "97466aaf-262c-4021-8398-28321001123b"
    private lateinit var server: MockWebServer
    private lateinit var db: TaskyDatabase
    private lateinit var syncDataSource: SyncDao
    private lateinit var localDataSource: ReminderDao
    private lateinit var retrofit: Retrofit
    private lateinit var remoteDataSource: TaskyReminderRemoteDataSource
    private lateinit var repository: TaskyReminderRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        server = MockWebServer()
        server.start(8080)
        retrofit = Retrofit.Builder()
            .baseUrl(server.url("http://127.0.0.1:8080"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        remoteDataSource = retrofit.create(TaskyReminderRemoteDataSource::class.java)
        db = Room.inMemoryDatabaseBuilder(context, TaskyDatabase::class.java).build()
        syncDataSource = db.syncDao()
        localDataSource = db.reminderDao()
        repository = TaskyReminderRepositoryImpl(remoteDataSource, localDataSource, syncDataSource, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getReminderFromRemoteDataSourceSuccess() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(200).setBody(reminderResponseToString())
        server.enqueue(response)
        val result = repository.getReminder(id)
        assertTrue(result is Result.Success)
        assertEquals((result as Result.Success).data, reminderResponseToPojo().asReminderDTO().asReminder())
    }

    @After
    fun tearDown() {
        db.close()
        server.shutdown()
    }
}