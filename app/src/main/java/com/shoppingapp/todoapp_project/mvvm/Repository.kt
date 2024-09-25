package com.shoppingapp.todoapp_project.mvvm

import androidx.lifecycle.LiveData
import com.shoppingapp.todoapp_project.db.TaskDao
import com.shoppingapp.todoapp_project.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(val dao: TaskDao) {

    val listTasks : LiveData<List<Task>> = dao.getAllTasks()

    val scope = CoroutineScope(Dispatchers.Main)

    fun addTask(task: Task) {
        scope.launch(Dispatchers.IO) {
            dao.addTask(task)
        }
    }

    fun deleteTask(task: Task) {
        scope.launch(Dispatchers.IO) {
            dao.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        scope.launch(Dispatchers.IO) {
            dao.updateTask(task)
        }
    }



}