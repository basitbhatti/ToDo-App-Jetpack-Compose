package com.shoppingapp.todoapp_project

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            "alarm_channel",
            "Alarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }
}