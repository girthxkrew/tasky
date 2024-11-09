package com.rkm.tasky.di.database

import android.content.Context
import androidx.room.Room
import com.rkm.tasky.database.TaskyDatabase
import com.rkm.tasky.database.dao.ReminderDao
import com.rkm.tasky.database.dao.SyncDao
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
    fun providesReminderDao(db: TaskyDatabase): ReminderDao {
        return db.reminderDao()
    }

    @Provides
    @Singleton
    fun providesSyncDao(db: TaskyDatabase): SyncDao {
        return db.syncDao()
    }

}