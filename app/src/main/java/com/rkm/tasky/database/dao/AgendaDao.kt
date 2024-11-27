package com.rkm.tasky.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.model.AgendaDetails
import com.rkm.tasky.database.model.AttendeeEntity
import com.rkm.tasky.database.model.EventAttendeesAndPhotosToDelete
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.EventWithDetails
import com.rkm.tasky.database.model.PhotoEntity
import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.database.model.TaskEntity

@Dao
interface AgendaDao {

    @Transaction
    @Query(
        """
    SELECT * FROM events WHERE  
    (
        ( (`from` >= :startTime) AND (`from` < :endTime) )
        OR
        ( (`to` > :startTime) AND (`to` <= :endTime) )
        OR
        ( (`from` <= :startTime) AND (`to` > :endTime) )
    )
        """
    )
    suspend fun getEventsInTimeRange(startTime: Long, endTime: Long): List<EventWithDetails>

    @Query(
        "SELECT * FROM tasks WHERE ((time >= :startTime) AND (time <= :endTime))"
    )
    suspend fun getTasksInTimeRange(startTime: Long, endTime: Long): List<TaskEntity>

    @Query(
        "SELECT * FROM reminders WHERE ((time >= :startTime) AND (time <= :endTime))"
    )
    suspend fun getRemindersInTimeRange(startTime: Long, endTime: Long): List<ReminderEntity>

    @Transaction
    suspend fun getAllAgendaDetailsInTimeRange(startTime: Long, endTime: Long): AgendaDetails {
        val events = getEventsInTimeRange(startTime, endTime)
        val reminders = getRemindersInTimeRange(startTime, endTime)
        val tasks = getTasksInTimeRange(startTime, endTime)
        return AgendaDetails(events, reminders, tasks)
    }

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun getAllEventIds(): List<EventWithDetails>

    @Query("SELECT * FROM reminders")
    suspend fun getAllReminderIds(): List<ReminderEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTaskIds(): List<TaskEntity>

    @Transaction
    suspend fun getAllAgendaItems(): AgendaDetails {
        val events = getAllEventIds()
        val reminders = getAllReminderIds()
        val tasks = getAllTaskIds()
        return AgendaDetails(events, reminders, tasks)
    }

    @Transaction
    suspend fun syncAgenda(
        db: TaskyDatabase,
        toDelete: AgendaDetails,
        agendaDetails: AgendaDetails,
        deleteDetails: EventAttendeesAndPhotosToDelete
    ) {
        db.apply {
            reminderDao().upsertReminders(agendaDetails.reminders)
            reminderDao().deleteRemindersById(toDelete.reminders.map { it.id })
            taskDao().upsertTasks(agendaDetails.tasks)
            taskDao().deleteTasksById(toDelete.tasks.map { it.id })
            eventDao().upsertEvents(agendaDetails.events.map { event -> event.event })
            agendaDetails.events.forEach { details ->
                attendeeDao().upsertAttendees(details.attendees)
                photoDao().upsertPhotos(details.photos)
            }
            deleteDetails.attendees.forEach { attendee ->
                attendeeDao().deleteByUserIdAndEventId(attendee.userId, attendee.eventId)
            }
            photoDao().deletePhotos(deleteDetails.photos.map { it.key })
            eventDao().deleteEventsByIds(toDelete.events.map { it.event.id })
        }
    }

}