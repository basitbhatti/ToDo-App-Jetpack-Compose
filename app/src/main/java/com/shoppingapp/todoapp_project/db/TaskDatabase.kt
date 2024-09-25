package com.shoppingapp.todoapp_project.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shoppingapp.todoapp_project.model.Task


@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getDao(): TaskDao

    companion object {
        var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, TaskDatabase::class.java, "tasksDB"
                    ).build()
                }

            }

            return INSTANCE!!
        }

    }


}