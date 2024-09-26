package com.shoppingapp.todoapp_project.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.shoppingapp.todoapp_project.R

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val title = intent?.getStringExtra("title")
        val desc = intent?.getStringExtra("desc")

        val channel_id = "alarm_channel"

        context?.also {
            val notificationManager = it.getSystemService(NotificationManager::class.java)

            val builder = NotificationCompat.Builder(it, channel_id)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            notificationManager.notify(1, builder.build())
        }

    }
}