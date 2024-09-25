package com.shoppingapp.todoapp_project.navigation

sealed class Screen (val route: String){

    object HomeScreen : Screen("home_screen")
    object AddTaskScreen : Screen("task_screen")

}