package com.rkm.tasky.alarm.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rkm.tasky.alarm.abstraction.ReminderAlarmManager
import com.rkm.tasky.di.ApplicationCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver: BroadcastReceiver() {

    @Inject
    lateinit var alarmManager: ReminderAlarmManager

    @Inject
    @ApplicationCoroutineScope lateinit var scope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.BOOT_COMPLETED") {
            scope.launch {
                //TODO: GET ALL AGENDA ITEMS
                //for
                //alarmManager.scheduleReminder(alarms)
            }
        }
    }
}