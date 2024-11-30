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
    private val event = EventEntity(
        id = "1",
        title = "Test Event",
        description = "Test Description",
        from = 0L,
        to = 0L,
        remindAt = 0L,
        host = "test2Id@gmail.com",
        isUserEventCreator = true
    )

    private val attendees = listOf(
        AttendeeEntity(
            userId = "12",
            email = "testId1@gmail.com",
            fullName = "Bob Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        ),
        AttendeeEntity(
            userId = "13",
            email = "test2Id@gmail.com",
            fullName = "Bobba Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        )
    )

    private val photos = listOf(
        PhotoEntity(
            key = "2",
            url = "www.google.com",
            eventId = "1"
        ),
        PhotoEntity(
            key = "3",
            url = "www.google.com",
            eventId = "1"
        )
    )

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

        eventDataSource.upsertEvent(event)
        attendeeDataSource.upsertAttendees(attendees)
        photoDataSource.upsertPhotos(photos)

        val result = eventDataSource.getAllEventDetails("1")

        if (result != null) {
            assertEquals(result.event, event)
            assertEquals(result.attendees.toHashSet(), attendees.toHashSet())
            assertEquals(result.photos.toHashSet(), photos.toHashSet())

        }
        assertTrue(result != null)
    }

    @Test
    fun insertEventDetails() = runTest {
        eventDataSource.upsertAllEventInfo(
            event = event,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        val eventResult = eventDataSource.getEventById(event.id)
        val attendeesResult = attendeeDataSource.getAttendeesByEventId(listOf(event.id))
        val photosResult = photoDataSource.getPhotosByEventId(listOf(event.id))

        assertEquals(eventResult, event)
        assertEquals(attendees.toHashSet(), attendeesResult.toHashSet())
        assertEquals(photos.toHashSet(), photosResult.toHashSet())
    }

    @Test
    fun updateNewAttendeesAndPhotosEventDetails() = runTest {
        val newAttendee = listOf(AttendeeEntity(
            userId = "14",
            email = "testId1@gmail.com",
            fullName = "Bobber Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        ))

        val newPhotos = listOf(PhotoEntity(
            key = "4",
            url = "www.google.com",
            eventId = "1"
        ))

        val newEvent = event.copy(title = "New Title")

        eventDataSource.upsertAllEventInfo(
            event = event,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        eventDataSource.upsertAllEventInfo(
            event = newEvent,
            attendees = newAttendee + attendees,
            photos = newPhotos + photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        val eventResult = eventDataSource.getEventById(event.id)
        val attendeesResult = attendeeDataSource.getAttendeesByEventId(listOf(event.id))
        val photosResult = photoDataSource.getPhotosByEventId(listOf(event.id))

        assertEquals(eventResult, newEvent)
        assertEquals((newAttendee + attendees).toHashSet(), attendeesResult.toHashSet())
        assertEquals((newPhotos + photos).toHashSet(), photosResult.toHashSet())
    }

    @Test
    fun updateRemoveOneAttendeesAndPhotosEventDetails() = runTest {
        val newAttendee = listOf(AttendeeEntity(
            userId = "14",
            email = "testId1@gmail.com",
            fullName = "Bobber Smith",
            eventId = "1",
            isGoing = true,
            remindAt = 0L
        ))

        val newPhotos = listOf(PhotoEntity(
            key = "4",
            url = "www.google.com",
            eventId = "1"
        ))

        val newEvent = event.copy(title = "New Title")

        eventDataSource.upsertAllEventInfo(
            event = event,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        eventDataSource.upsertAllEventInfo(
            event = newEvent,
            attendees = newAttendee + attendees.last(),
            photos = newPhotos + photos.last(),
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        val eventResult = eventDataSource.getEventById(event.id)
        val attendeesResult = attendeeDataSource.getAttendeesByEventId(listOf(event.id))
        val photosResult = photoDataSource.getPhotosByEventId(listOf(event.id))

        assertEquals(eventResult, newEvent)
        assertEquals((newAttendee + attendees.last()).toHashSet(), attendeesResult.toHashSet())
        assertEquals((newPhotos + photos.last()).toHashSet(), photosResult.toHashSet())
    }

    @Test
    fun updatePassEmptyListEventDetails() = runTest {

        val newEvent = event.copy(title = "New Title")

        eventDataSource.upsertAllEventInfo(
            event = event,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        eventDataSource.upsertAllEventInfo(
            event = newEvent,
            attendees = emptyList(),
            photos = emptyList(),
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        val eventResult = eventDataSource.getEventById(event.id)
        val attendeesResult = attendeeDataSource.getAttendeesByEventId(listOf(event.id))
        val photosResult = photoDataSource.getPhotosByEventId(listOf(event.id))

        assertEquals(eventResult, newEvent)
        assertTrue(attendeesResult.isEmpty())
        assertTrue(photosResult.isEmpty())
    }

    @Test
    fun updateSameAttendeesAndPhotosEventDetails() = runTest {

        val newEvent = event.copy(title = "New Title")

        eventDataSource.upsertAllEventInfo(
            event = event,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        eventDataSource.upsertAllEventInfo(
            event = newEvent,
            attendees = attendees,
            photos = photos,
            photoDao = photoDataSource,
            attendeeDao = attendeeDataSource
        )

        val eventResult = eventDataSource.getEventById(event.id)
        val attendeesResult = attendeeDataSource.getAttendeesByEventId(listOf(event.id))
        val photosResult = photoDataSource.getPhotosByEventId(listOf(event.id))

        assertEquals(eventResult, newEvent)
        assertEquals(attendees.toHashSet(), attendeesResult.toHashSet())
        assertEquals(photos.toHashSet(), photosResult.toHashSet())
    }

    @After
    fun tearDown() {
        db.close()
    }
}