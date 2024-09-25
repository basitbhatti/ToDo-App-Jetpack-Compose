package com.shoppingapp.todoapp_project.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shoppingapp.todoapp_project.model.Task

@Dao
interface TaskDao  {

    @Insert
    suspend fun addTask(task : Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks() : LiveData<List<Task>>


}