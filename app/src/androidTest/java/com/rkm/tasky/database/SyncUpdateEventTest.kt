package com.rkm.tasky.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncDeletePhotoDao
import com.rkm.tasky.database.dao.SyncUpdateEventDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncDeletePhotoEntity
import com.rkm.tasky.database.model.SyncUpdateEventEntity
import com.rkm.tasky.database.model.SyncUploadPhotoEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SyncUpdateEventTest {
    private lateinit var db: TaskyDatabase
    private lateinit var syncUpdateEventDataSource: SyncUpdateEventDao
    private lateinit var syncAttendeeDataSource: SyncAttendeeDao
    private lateinit var syncPhotoUploadDataSource: SyncUploadPhotoDao
    private lateinit var syncDeleteUploadDataSource: SyncDeletePhotoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskyDatabase::class.java).build()
        syncUpdateEventDataSource = db.syncUpdateEventDao()
        syncAttendeeDataSource = db.syncAttendeeDao()
        syncPhotoUploadDataSource = db.syncUploadPhotoDao()
        syncDeleteUploadDataSource = db.syncDeletePhotoDao()
    }

    @Test
    fun getSyncUpdateEventWithDetails() = runTest {
        val attendees = listOf(
            SyncAttendeeEntity(
                eventId = "1",
                userId = "id1"
            )
        )

        val deletedPhotos = listOf(
            SyncDeletePhotoEntity(
                eventId = "1",
                photoKey = "5"
            )
        )

        val photos = listOf(
            SyncUploadPhotoEntity(
                eventId = "1",
                filePath = "filepath1"
            )
        )

        val event = SyncUpdateEventEntity(
            id = "1",
            title = "title",
            description = "description",
            from = 0L,
            to = 0L,
            remindAt = 0L,
            isGoing = false
        )

        syncUpdateEventDataSource.upsertEvent(event)
        syncAttendeeDataSource.upsertAttendees(attendees)
        syncPhotoUploadDataSource.upsertPhotos(photos)
        syncDeleteUploadDataSource.upsertPhotoKeys(deletedPhotos)

        val results = syncUpdateEventDataSource.getEventDetailsById(listOf("1"))
        assertTrue(results.isNotEmpty())
        assertEquals(results.first().event, event)
        assertEquals(results.first().photos.first().eventId, photos.first().eventId)
        assertEquals(results.first().photos.first().filePath, photos.first().filePath)
        assertEquals(results.first().attendees.first().eventId, attendees.first().eventId)
        assertEquals(results.first().attendees.first().userId, attendees.first().userId)
        assertEquals(results.first().photoKeys.first().eventId, deletedPhotos.first().eventId)
        assertEquals(results.first().photoKeys.first().photoKey, deletedPhotos.first().photoKey)
    }

    @After
    fun tearDown() {
        db.close()
    }
}