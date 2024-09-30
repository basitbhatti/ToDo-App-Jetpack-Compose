package com.shoppingapp.todoapp_project.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoppingapp.todoapp_project.model.Task
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainViewModel(val repository: Repository) : ViewModel() {

    val taskList: LiveData<List<Task>> = repository.listTasks

    init {
        taskList.observeForever { tasks->
            categorizeLists(tasks, LocalDate.now())
        }
    }

    private val _listCompleteTasks: MutableLiveData<List<Task>> = MutableLiveData()
    private val _listIncompleteTasks: MutableLiveData<List<Task>> = MutableLiveData()
    private val _listOverdueTasks: MutableLiveData<List<Task>> = MutableLiveData()

    val listCompleteTasks: LiveData<List<Task>> = _listCompleteTasks
    val listIncompleteTasks: LiveData<List<Task>> = _listIncompleteTasks
    val listOverdueTasks: LiveData<List<Task>> = _listOverdueTasks

    fun categorizeLists(taskList: List<Task>, dateToday: LocalDate) {

        val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val complete = ArrayList<Task>()
        val incomplete = ArrayList<Task>()
        val overdue = ArrayList<Task>()

        taskList.forEach { task ->

            val dueDate = LocalDate.parse(task.dueDate, formatter)
            val dueTime = LocalTime.parse(task.dueTime, formatterTime)

            if (task.isCompleted) {
                complete.add(task)
            } else if (dueDate.isBefore(dateToday)) {
                overdue.add(task)
            } else if (dueDate.isEqual(dateToday) && dueTime.isBefore(LocalTime.now())) {
                overdue.add(task)
            } else {
                incomplete.add(task)
                Log.d("TAGTask", "task's due date and time : $dueDate $dueTime is after today $dateToday ${LocalTime.now()}")
            }

        }

        _listCompleteTasks.value = complete
        _listIncompleteTasks.value = incomplete
        _listOverdueTasks.value = overdue
    }

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }

}