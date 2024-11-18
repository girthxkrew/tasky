package com.rkm.tasky.alarm.model

import android.os.Parcel
import android.os.Parcelable
import com.rkm.tasky.feature.common.AgendaItemType
import com.rkm.tasky.util.date.getCurrentDayInLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReminderAlarm(
    val id: String,
    val title: String,
    val desc: String? = null,
    val time: LocalDateTime,
    val type: AgendaItemType
): Parcelable {
    private companion object : Parceler<ReminderAlarm> {
        override fun ReminderAlarm.write(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(title)
            parcel.writeString(desc)
            parcel.writeString(time.toString())
            parcel.writeString(type.name)
        }

        override fun create(parcel: Parcel): ReminderAlarm {
            val id = parcel.readString() ?: ""
            val title = parcel.readString() ?: ""
            val desc = parcel.readString() ?: ""
            val stringTime = parcel.readString()
            val time = if(stringTime.isNullOrEmpty()) getCurrentDayInLocalDateTime() else LocalDateTime.parse(stringTime)
            val type = AgendaItemType.valueOf(parcel.readString() ?: AgendaItemType.REMINDER.name)
            return ReminderAlarm(
                id = id,
                title = title,
                desc = desc,
                time = time,
                type = type
            )
        }
    }

}
