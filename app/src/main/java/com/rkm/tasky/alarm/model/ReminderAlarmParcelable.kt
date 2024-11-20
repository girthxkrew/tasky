package com.rkm.tasky.alarm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReminderAlarmParcelable(
    val id: String,
    val title: String,
    val desc: String?,
    val time: Long,
    val type: String
): Parcelable

