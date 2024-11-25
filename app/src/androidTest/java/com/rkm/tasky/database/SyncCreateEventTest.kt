package com.rkm.tasky.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncCreateEventDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.model.SyncAttendeeEntity
import com.rkm.tasky.database.model.SyncCreateEventEntity
import com.rkm.tasky.database.model.SyncUploadPhotoEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SyncCreateEventTest {

    private lateinit var db: TaskyDatabase
    private lateinit var syncCreateEventDataSource: SyncCreateEventDao
    private lateinit var syncAttendeeDataSource: SyncAttendeeDao
    private lateinit var syncPhotoUploadDataSource: SyncUploadPhotoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskyDatabase::class.java).build()
        syncCreateEventDataSource = db.syncCreateEventDao()
        syncAttendeeDataSource = db.syncAttendeeDao()
        syncPhotoUploadDataSource = db.syncUploadPhotoDao()
    }

    @Test
    fun getSyncCreateEventWithDetails() = runTest {
        val attendees = listOf(
            SyncAttendeeEntity(
                eventId = "1",
                userId = "id1"
            )
        )

        val photos = listOf(
            SyncUploadPhotoEntity(
                eventId = "1",
                filePath = "filepath1"
            )
        )

        val event = SyncCreateEventEntity(
            id = "1",
            title = "title",
            description = "description",
            from = 0L,
            to = 0L,
            remindAt = 0L
        )

        syncCreateEventDataSource.upsertEvent(event)
        syncAttendeeDataSource.upsertAttendees(attendees)
        syncPhotoUploadDataSource.upsertPhotos(photos)

        val results = syncCreateEventDataSource.getEventDetailsById(listOf("1"))
        assertTrue(results.isNotEmpty())
        assertEquals(results.first().event, event)
        assertEquals(results.first().photos.first().eventId, photos.first().eventId)
        assertEquals(results.first().photos.first().filePath, photos.first().filePath)
        assertEquals(results.first().attendees.first().eventId, attendees.first().eventId)
        assertEquals(results.first().attendees.first().userId, attendees.first().userId)
    }

    @After
    fun tearDown() {
        db.close()
    }
}