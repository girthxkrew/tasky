package com.rkm.tasky.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.TaskDao
import com.rkm.tasky.database.model.EventEntity
import com.rkm.tasky.database.model.ReminderEntity
import com.rkm.tasky.database.model.SyncConverter
import com.rkm.tasky.database.model.SyncEntity
import com.rkm.tasky.database.model.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        EventEntity::class,
        ReminderEntity::class,
        SyncEntity::class
    ],
    version = 1
)
@TypeConverters(SyncConverter::class)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
    abstract fun syncDao(): SyncDao
}