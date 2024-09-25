package com.shoppingapp.todoapp_project.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shoppingapp.todoapp_project.db.TaskDatabase
import com.shoppingapp.todoapp_project.mvvm.Repository
import com.shoppingapp.todoapp_project.ui.screens.AddTaskScreen
import com.shoppingapp.todoapp_project.ui.screens.HomeScreen

@Composable
fun NavGraphBuilder(navHostController: NavHostController, context : Context) {


    NavHost(navController = navHostController, startDestination = Screen.HomeScreen.route){

        composable(Screen.HomeScreen.route){
            HomeScreen(navController = navHostController)
        }

        composable(Screen.AddTaskScreen.route){
            val repository = Repository(TaskDatabase.getInstance(context).getDao())
            AddTaskScreen(navController = navHostController, repository = repository)
        }


    }

}