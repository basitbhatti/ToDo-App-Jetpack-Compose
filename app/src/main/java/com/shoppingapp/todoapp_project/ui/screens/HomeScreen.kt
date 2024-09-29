package com.shoppingapp.todoapp_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.compose.rememberNavController
import com.shoppingapp.todoapp_project.R
import com.shoppingapp.todoapp_project.db.TaskDatabase
import com.shoppingapp.todoapp_project.model.TabItem
import com.shoppingapp.todoapp_project.model.Task
import com.shoppingapp.todoapp_project.mvvm.MainVMFactory
import com.shoppingapp.todoapp_project.mvvm.MainViewModel
import com.shoppingapp.todoapp_project.mvvm.Repository
import com.shoppingapp.todoapp_project.navigation.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.background(Color(0xFFF9F9F9)), navController: NavHostController
) {

    Scaffold(modifier = modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate(Screen.AddTaskScreen.route)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task Buton")
        }
    }) {

        Column (
            modifier = modifier.padding(it)
        ) {

            val repository = Repository(TaskDatabase.getInstance(LocalContext.current).getDao())

            val viewModel: MainViewModel = viewModel(
                factory = MainVMFactory(repository)
            )

            val listCompletedTasks = viewModel.listCompleteTasks.observeAsState(emptyList())
            val listIncompleteTasks = viewModel.listIncompleteTasks.observeAsState(emptyList())
            val listOverdueTasks = viewModel.listOverdueTasks.observeAsState(emptyList())

            val listTabs = listOf(
                TabItem(
                    title = "ToDo",
                    selectedIcon = Icons.Filled.List,
                    unselectedIcon = Icons.Outlined.List
                ), TabItem(
                    title = "Overdue",
                    selectedIcon = Icons.Filled.DateRange,
                    unselectedIcon = Icons.Outlined.DateRange
                ), TabItem(
                    title = "Completed",
                    selectedIcon = Icons.Filled.Done,
                    unselectedIcon = Icons.Outlined.Done
                )
            )

            var selectedTabIndex by remember {
                mutableIntStateOf(0)
            }

            var pagerState = rememberPagerState {
                listTabs.size
            }

            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }

            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }

            val listOfLists = ArrayList<List<Task>>()
            listOfLists.add(listIncompleteTasks.value)
            listOfLists.add(listOverdueTasks.value)
            listOfLists.add(listCompletedTasks.value)

            TabRow(selectedTabIndex = selectedTabIndex) {

                listTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        }, text = {
                            Text(text = tab.title)
                        }, icon = {
                            Icon(
                                imageVector = if (selectedTabIndex == index) {
                                    tab.selectedIcon
                                } else tab.unselectedIcon, contentDescription = tab.title
                            )
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
            ) { index ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listOfLists.get(selectedTabIndex)) { task ->
                        TaskItem(
                            task = task, repository = repository
                        )
                    }
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
        ), modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(10.dp)
    ) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Row (
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

                Box(modifier = Modifier.weight(20f), contentAlignment = Alignment.Center) {
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
                color = Color.LightGray,
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

//    TaskItem(
//        task = Task(0, "This is a task", "Description....", "", "", false),
//        repository = repository
//    )

    HomeScreen(navController = rememberNavController())


}
