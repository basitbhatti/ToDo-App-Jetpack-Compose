package com.shoppingapp.todoapp_project.alarm

interface AlarmScheduler {

    fun schedule (alarmItem: AlarmItem)
    fun cancel (alarmItem: AlarmItem)
}