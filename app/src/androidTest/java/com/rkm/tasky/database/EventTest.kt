package com.rkm.tasky.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rkm.tasky.database.dao.AttendeeDao
import com.rkm.tasky.database.dao.EventDao
import com.rkm.tasky.database.dao.PhotoDao
import com.rkm.tasky.database.model.AttendeeEntity
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.PhotoEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventTest {

    private lateinit var db: TaskyDatabase
    private lateinit var eventDataSource: EventDao
    private lateinit var attendeeDataSource: AttendeeDao
    private lateinit var photoDataSource: PhotoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskyDatabase::class.java).build()
        eventDataSource = db.eventDao()
        attendeeDataSource = db.attendeeDao()
        photoDataSource = db.photoDao()
    }

    @Test
    fun getEventDetailsResults() = runTest {
        val attendee1 = AttendeeEntity(
            userId = "12",
            email = "testId1@gmail.com",
            fullName = "Bob Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        )

        val attendee2 = AttendeeEntity(
            userId = "13",
            email = "test2Id@gmail.com",
            fullName = "Bobba Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        )

        val photo1 = PhotoEntity(
            key = "2",
            url = "www.google.com",
            eventId = "1"
        )

        val photo2 = PhotoEntity(
            key = "3",
            url = "www.google.com",
            eventId = "1"
        )

        val event = EventEntity(
            id = "1",
            title = "Test Event",
            description = "Test Description",
            from = 0L,
            to = 0L,
            remindAt = 0L,
            host = "test2Id@gmail.com",
            isUserEventCreator = true
        )

        val attendees = listOf(attendee1, attendee2)
        val photos = listOf(photo1, photo2)

        eventDataSource.upsertEvent(event)
        attendeeDataSource.upsertAttendees(listOf(attendee1, attendee2))
        photoDataSource.upsertPhotos(listOf(photo1, photo2))

        val result = eventDataSource.getAllEventDetails("1")

        if (result != null) {
            assertEquals(result.event, event)
            assertEquals(result.attendees.toHashSet(), attendees.toHashSet())
            assertEquals(result.photos.toHashSet(), photos.toHashSet())

        }
        assertTrue(result != null)
    }

    @After
    fun tearDown() {
        db.close()
    }
}