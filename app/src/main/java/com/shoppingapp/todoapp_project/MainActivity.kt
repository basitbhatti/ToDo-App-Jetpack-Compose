package com.shoppingapp.todoapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.shoppingapp.todoapp_project.navigation.NavGraphBuilder
import com.shoppingapp.todoapp_project.ui.theme.TodoAppProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppProjectTheme {

                val navController = rememberNavController()
                NavGraphBuilder(navHostController = navController, this@MainActivity)

            }
        }
    }
}
