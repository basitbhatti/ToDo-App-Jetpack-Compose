package com.shoppingapp.todoapp_project

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val channel = "alarm_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notificationChannel =
            NotificationChannel(channel, channel, NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(notificationChannel)
    }
}