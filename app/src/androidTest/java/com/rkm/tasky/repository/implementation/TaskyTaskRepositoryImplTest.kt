package com.rkm.tasky.repository.implementation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.model.SyncItemType
import com.rkm.tasky.database.model.SyncUserAction
import com.rkm.tasky.network.datasource.TaskyTaskRemoteDataSource
import com.rkm.tasky.repository.mapper.asTask
import com.rkm.tasky.repository.mapper.asTaskEntity
import com.rkm.tasky.util.result.Result
import com.rkm.tasky.utils.json.errorMessageToString
import com.rkm.tasky.utils.json.taskResponseToPojo
import com.rkm.tasky.utils.json.taskResponseToString
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class TaskyTaskRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val id = "97466aaf-262c-4021-8398-28321001123b"
    private lateinit var server: MockWebServer
    private lateinit var db: TaskyDatabase
    private lateinit var syncDataSource: SyncDao
    private lateinit var retrofit: Retrofit
    private lateinit var localDataSource: TaskDao
    private lateinit var remoteDataSource: TaskyTaskRemoteDataSource
    private lateinit var repository: TaskyTaskRepositoryImpl

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
        remoteDataSource = retrofit.create(TaskyTaskRemoteDataSource::class.java)
        syncDataSource = db.syncDao()
        localDataSource = db.taskDao()
        repository = TaskyTaskRepositoryImpl(remoteDataSource, localDataSource, syncDataSource, dispatcher)
    }

    @Test
    fun getTaskFromRemoteDataSourceSuccess() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(200).setBody(taskResponseToString())
        server.enqueue(response)
        val result = repository.getTask(id)
        assertTrue(result is Result.Success)
        assertEquals((result as Result.Success).data, taskResponseToPojo().asTask())
    }

    @Test
    fun getTaskFromRemoteDataSourceFailure() = runTest(dispatcher) {
        localDataSource.upsertTask(taskResponseToPojo().asTaskEntity())
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.getTask(id)
        assertTrue(result is Result.Success)
        assertEquals((result as Result.Success).data, localDataSource.getTaskById(id)!!.asTask())
    }

    @Test
    fun createTaskFromRemoteDataSourceSuccess() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        val result = repository.createTask(taskResponseToPojo().asTask())
        assertTrue(result is Result.Success)
        assertNotNull(localDataSource.getTaskById(id))
    }

    @Test
    fun createTaskFromRemoteDataSourceFailure() = runTest(dispatcher) {
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.createTask(taskResponseToPojo().asTask())
        assertTrue(result is Result.Error)
        assertNotNull(localDataSource.getTaskById(id))
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.TASK)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.CREATE)
    }

    @Test
    fun updateTaskFromRemoteDataSourceSuccess() = runTest(dispatcher) {
        val task = taskResponseToPojo().copy(title = "new title")
        localDataSource.upsertTask(taskResponseToPojo().asTaskEntity())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        assertEquals(localDataSource.getTaskById(id), taskResponseToPojo().asTaskEntity())
        val result = repository.updateTask(task.asTask())
        assertTrue(result is Result.Success)
        assertTrue(localDataSource.getTaskById(id)!!.title == "new title")
    }

    @Test
    fun updateTaskFromRemoteDataSourceFailure() = runTest(dispatcher) {
        val task = taskResponseToPojo().copy(title = "new title")
        localDataSource.upsertTask(taskResponseToPojo().asTaskEntity())
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.updateTask(task.asTask())
        assertTrue(result is Result.Error)
        assertEquals(localDataSource.getTaskById(id)!!.title, "new title")
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.TASK)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.UPDATE)
    }

    @Test
    fun deleteTaskFromRemoteDataSourceSuccess() = runTest(dispatcher) {
        localDataSource.upsertTask(taskResponseToPojo().asTaskEntity())
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        val result = repository.deleteTask(taskResponseToPojo().asTask())
        assertTrue(result is Result.Success)
        assertNull(localDataSource.getTaskById(id))
    }

    @Test
    fun deleteTaskFromRemoteDataSourceFailure() = runTest(dispatcher) {
        localDataSource.upsertTask(taskResponseToPojo().asTaskEntity())
        val response = MockResponse().setResponseCode(401).setBody(errorMessageToString())
        server.enqueue(response)
        val result = repository.deleteTask(taskResponseToPojo().asTask())
        assertTrue(result is Result.Error)
        assertNull(localDataSource.getTaskById(id))
        assertNotNull(syncDataSource.getSyncItemById(id))
        assertTrue(syncDataSource.getSyncItemById(id).item == SyncItemType.TASK)
        assertTrue(syncDataSource.getSyncItemById(id).action == SyncUserAction.DELETE)
    }

    @After
    fun tearDown() {
        server.shutdown()
        db.close()
    }
}