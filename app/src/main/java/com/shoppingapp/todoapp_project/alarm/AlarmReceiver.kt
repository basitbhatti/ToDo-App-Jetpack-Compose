package com.shoppingapp.todoapp_project.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.shoppingapp.todoapp_project.R

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSAGE") ?: return
        val channel_id = "alarm_channel"

        context?.also { ctx ->

            val notificationManager =
                ctx.getSystemService(NotificationManager::class.java) as NotificationManager

            val builder = NotificationCompat.Builder(ctx, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationManager.notify(1, builder.build())

        }


    }
}