package com.rkm.tasky.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.database.model.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        EventEntity::class,
        ReminderEntity::class
    ],
    version = 1
)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
}