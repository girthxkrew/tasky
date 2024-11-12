package com.rkm.tasky.repository.implementation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.network.datasource.TaskyReminderRemoteDataSource
import com.rkm.tasky.repository.mapper.asReminder
import com.rkm.tasky.repository.mapper.asReminderEntity
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.utils.json.errorMessageToString
import com.rkm.tasky.utils.json.reminderResponseToPojo
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
        db = Room.inMemoryDatabaseBuilder(context, TaskyDatabase::class.java).build()
        server = MockWebServer()
        server.start()

        retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        remoteDataSource = retrofit.create(TaskyReminderRemoteDataSource::class.java)
        syncDataSource = db.syncDao()
        localDataSource = db.reminderDao()
        repository = TaskyReminderRepositoryImpl(remoteDataSource, localDataSource, syncDataSource, dispatcher)
    }

    @Test
    fun getReminderRepositorySuccess() = runTest(dispatcher) {
        localDataSource.upsertReminder(reminderResponseToPojo().asReminderEntity())
        val result = repository.getReminder(id)
        assertTrue(result is Result.Success)
        assertEquals((result as Result.Success).data, localDataSource.getReminderById(id)!!.asReminder())
    }

    @Test
    fun createReminderRepositorySuccess() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        val result = repository.createReminder(reminderResponseToPojo().asReminder())
        assertTrue(result is Result.Success)
        assertNotNull(localDataSource.getReminderById(id))
    }

    @Test
    fun createReminderRepositoryFailure() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.createReminder(reminderResponseToPojo().asReminder())
        assertTrue(result is Result.Error)
        assertNotNull(localDataSource.getReminderById(id))
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.REMINDER)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.CREATE)
    }

    @Test
    fun updateReminderRepositorySuccess() = runTest(dispatcher) {
        val reminder = reminderResponseToPojo().copy(title = "new title")
        localDataSource.upsertReminder(reminderResponseToPojo().asReminderEntity())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        assertEquals(localDataSource.getReminderById(id), reminderResponseToPojo().asReminderEntity())
        val result = repository.updateReminder(reminder.asReminder())
        assertTrue(result is Result.Success)
        assertTrue(localDataSource.getReminderById(id)!!.title == "new title")
    }

    @Test
    fun updateReminderRepositoryFailure() = runTest(dispatcher) {
        val reminder = reminderResponseToPojo().copy(title = "new title")
        localDataSource.upsertReminder(reminderResponseToPojo().asReminderEntity())
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.updateReminder(reminder.asReminder())
        assertTrue(result is Result.Error)
        assertEquals(localDataSource.getReminderById(id)!!.title, "new title")
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.REMINDER)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.UPDATE)
    }

    @Test
    fun deleteReminderRepositorySuccess() = runTest(dispatcher) {
        localDataSource.upsertReminder(reminderResponseToPojo().asReminderEntity())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        val result = repository.deleteReminder(reminderResponseToPojo().asReminder())
        assertTrue(result is Result.Success)
        assertNull(localDataSource.getReminderById(id))
    }

    @Test
    fun deleteReminderRepositoryFailure() = runTest(dispatcher) {
        localDataSource.upsertReminder(reminderResponseToPojo().asReminderEntity())
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.deleteReminder(reminderResponseToPojo().asReminder())
        assertTrue(result is Result.Error)
        assertNull(localDataSource.getReminderById(id))
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.REMINDER)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.DELETE)
    }

    @After
    fun tearDown() {
        db.close()
        server.shutdown()
    }
}