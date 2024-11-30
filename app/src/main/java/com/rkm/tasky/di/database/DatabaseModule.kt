package com.rkm.tasky.di.database

import android.content.Context
import androidx.room.Room
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.AgendaDao
import com.rkm.tasky.database.dao.AttendeeDao
import com.rkm.tasky.database.dao.EventDao
import com.rkm.tasky.database.dao.PhotoDao
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncAttendeeDao
import com.rkm.tasky.database.dao.SyncCreateEventDao
import com.rkm.tasky.database.dao.SyncDao
import com.rkm.tasky.database.dao.SyncDeletePhotoDao
import com.rkm.tasky.database.dao.SyncUpdateEventDao
import com.rkm.tasky.database.dao.SyncUploadPhotoDao
import com.rkm.tasky.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "tasky_db"

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): TaskyDatabase {
        return Room.databaseBuilder(
            context,
            TaskyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesAgendaDao(db: TaskyDatabase): AgendaDao {
        return db.agendaDao()
    }

    @Provides
    @Singleton
    fun providesReminderDao(db: TaskyDatabase): ReminderDao {
        return db.reminderDao()
    }

    @Provides
    @Singleton
    fun providesSyncDao(db: TaskyDatabase): SyncDao {
        return db.syncDao()
    }

    @Provides
    @Singleton
    fun providesTaskDao(db: TaskyDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun providesEventDao(db: TaskyDatabase): EventDao {
        return db.eventDao()
    }

    @Provides
    @Singleton
    fun providesAttendeeDao(db: TaskyDatabase): AttendeeDao {
        return db.attendeeDao()
    }

    @Provides
    @Singleton
    fun providesPhotosDao(db: TaskyDatabase): PhotoDao {
        return db.photoDao()
    }

    @Provides
    @Singleton
    fun providesSyncAttendeeDao(db: TaskyDatabase): SyncAttendeeDao {
        return db.syncAttendeeDao()
    }

    @Provides
    @Singleton
    fun providesSyncCreateEventDao(db: TaskyDatabase): SyncCreateEventDao {
        return db.syncCreateEventDao()
    }

    @Provides
    @Singleton
    fun providesSyncDeletePhotoDao(db: TaskyDatabase): SyncDeletePhotoDao{
        return db.syncDeletePhotoDao()
    }

    @Provides
    @Singleton
    fun providesSyncUpdateEventDao(db: TaskyDatabase): SyncUpdateEventDao {
        return db.syncUpdateEventDao()
    }

    @Provides
    @Singleton
    fun providesSyncUploadPhotoDao(db: TaskyDatabase): SyncUploadPhotoDao {
        return db.syncUploadPhotoDao()
    }
}