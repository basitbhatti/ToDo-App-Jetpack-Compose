package com.shoppingapp.todoapp_project.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

class AlarmImpl (
    val context : Context
) : AlarmScheduler {

    lateinit var pendingIntent: PendingIntent

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarmItem: AlarmItem) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MESSAGE", alarmItem.message)
        }
        val alarmTime = alarmItem.dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()*1000L

        pendingIntent = PendingIntent.getBroadcast(context, alarmItem.hashCode(), intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )


    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}