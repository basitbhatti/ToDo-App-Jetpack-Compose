package com.shoppingapp.todoapp_project.alarm

import java.time.LocalDateTime

data class AlarmItem (
    val alarmTime : LocalDateTime,
    val title : String,
    val description : String
)