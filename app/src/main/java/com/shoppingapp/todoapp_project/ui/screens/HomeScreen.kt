package com.shoppingapp.todoapp_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shoppingapp.todoapp_project.R
import com.shoppingapp.todoapp_project.db.TaskDatabase
import com.shoppingapp.todoapp_project.model.Task
import com.shoppingapp.todoapp_project.mvvm.MainVMFactory
import com.shoppingapp.todoapp_project.mvvm.MainViewModel
import com.shoppingapp.todoapp_project.mvvm.Repository
import com.shoppingapp.todoapp_project.navigation.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier.background(Color(0xFFF9F9F9)), navController: NavHostController) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddTaskScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task Buton")
            }
        }
    ) {

        Column(
            modifier = modifier.padding(it)
        ) {

            val task = Task(0, "Do something.", "At your office", "Sept 26 2024", "12:12", false)

            val repository = Repository(TaskDatabase.getInstance(LocalContext.current).getDao())

            val viewModel : MainViewModel = viewModel (
                factory = MainVMFactory(repository)
            )

            val listToDos by viewModel.taskList.observeAsState(emptyList())

            LazyColumn {
                items(listToDos){ task->
                    TaskItem(
                        task = task,
                        repository = repository
                    )
                }
            }
        }
    }

}

@Composable
fun TaskItem(modifier: Modifier = Modifier, task: Task, repository: Repository) {


    val viewModel: MainViewModel = viewModel(
        factory = MainVMFactory(repository)
    )

    var isCompleted by remember {
        mutableStateOf(task.isCompleted)
    }


    var todaysDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedTodaysDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(todaysDate)
        }
    }

    var dueDate by remember {
        mutableStateOf(task.dueDate)
    }

    val displayDateText = if (dueDate == formattedTodaysDate) {
        "Today"
    } else {
        dueDate
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(10.dp)
    ) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.weight(80f)) {
                    Text(
                        text = task.title,
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.cereal_bold))
                    )
                }

                Box(modifier = Modifier.weight(20f)) {
                    Checkbox(checked = isCompleted, onCheckedChange = {
                        val newTask = task
                        newTask.isCompleted = it
                        isCompleted = it

                        viewModel.updateTask(newTask)

                    })
                }
            }

            if (task.description != null) {
                Text(
                    text = task.description,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.cereal_med)),
                    modifier = modifier.padding(start = 15.dp)
                )
            }

            Divider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 7.5.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = displayDateText,
                    fontSize = 18.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily(Font(R.font.cereal_bold))
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = task.dueTime,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.cereal_med))
                )

            }

        }

    }

}

@Preview
@Composable
private fun Preview() {
    val repository = Repository(TaskDatabase.getInstance(LocalContext.current).getDao())

    TaskItem(
        task = Task(0, "This is a task", "Description....", "", "", false),
        repository = repository
    )
}
