package com.rkm.tasky.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "sync")
data class SyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val action: SyncUserAction,
    val item: SyncItemType,
    val itemId: String
)

enum class SyncUserAction() {
    CREATE,
    UPDATE,
    DELETE
}

enum class SyncItemType {
    REMINDER,
    TASK,
    EVENT
}

class SyncConverter {

    @TypeConverter
    fun fromAction(action: SyncUserAction): String {
        return action.name
    }

    @TypeConverter
    fun toAction(action: String): SyncUserAction {
        return SyncUserAction.valueOf(action)
    }

    @TypeConverter
    fun fromItem(item: SyncItemType): String {
        return item.name
    }

    @TypeConverter
    fun toItem(item: String): SyncItemType {
        return SyncItemType.valueOf(item)
    }
}
