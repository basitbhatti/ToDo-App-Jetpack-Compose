package com.shoppingapp.todoapp_project.alarm

import java.time.LocalDateTime

data class AlarmItem(
    val dateTime: LocalDateTime, val message: String
)