package com.shoppingapp.todoapp_project.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shoppingapp.todoapp_project.model.Task

class MainViewModel(val repository: Repository) : ViewModel() {

    val taskList : LiveData<List<Task>> = repository.listTasks

    fun addTask (task: Task){
        repository.addTask(task)
    }

    fun updateTask(task: Task){
        repository.updateTask(task)
    }

    fun deleteTask(task: Task){
        repository.deleteTask(task)
    }


}