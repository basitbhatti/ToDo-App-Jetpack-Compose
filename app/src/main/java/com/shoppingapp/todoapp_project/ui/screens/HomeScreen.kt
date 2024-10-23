package com.shoppingapp.todoapp_project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shoppingapp.todoapp_project.R
import com.shoppingapp.todoapp_project.alarm.AlarmItem
import com.shoppingapp.todoapp_project.alarm.AlarmScheduler
import com.shoppingapp.todoapp_project.alarm.AlarmSchedulerImpl
import com.shoppingapp.todoapp_project.db.TaskDatabase
import com.shoppingapp.todoapp_project.model.TabItem
import com.shoppingapp.todoapp_project.model.Task
import com.shoppingapp.todoapp_project.mvvm.MainVMFactory
import com.shoppingapp.todoapp_project.mvvm.MainViewModel
import com.shoppingapp.todoapp_project.mvvm.Repository
import com.shoppingapp.todoapp_project.ui.theme.TodoAppProjectTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background),
    navController: NavHostController,
    repository: Repository
) {

    var isVisible by remember {
        mutableStateOf(false)
    }

    var isDateSelected by remember {
        mutableStateOf(false)
    }

    var isTimeSelected by remember {
        mutableStateOf(false)
    }

    var textColorDate = if (isDateSelected) {
        Color.Black
    } else {
        Color.Gray
    }

    var textColorTime = if (isTimeSelected) {
        Color.Black
    } else {
        Color.Gray
    }

    var textTitle by remember {
        mutableStateOf("")
    }

    var textDescription by remember {
        mutableStateOf("")
    }

    var textIsCompleted by remember {
        mutableStateOf(false)
    }

    var dueDate by remember {
        mutableStateOf(LocalDate.now())
    }

    var dueTime by remember {
        mutableStateOf(LocalTime.NOON)
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(dueDate)
        }
    }

    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm").format(dueTime)
        }
    }

    var timePickerState = rememberMaterialDialogState()
    var datePickerState = rememberMaterialDialogState()
    val snackbarHostState = SnackbarHostState()
    val viewModel: MainViewModel = viewModel(
        factory = MainVMFactory(repository)
    )

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    TodoAppProjectTheme {
        Scaffold(modifier = modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                onClick = {
                    isVisible = true
//                    navController.navigate(Screen.AddTaskScreen.route)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task Button")
            }
        }) {
            Column(
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
                    TabItem (
                        title = "ToDo",
                        selectedIcon = painterResource(id = R.drawable.ic_todo_filled),
                        unselectedIcon = painterResource(id = R.drawable.ic_todo_outlined)
                    ), TabItem (
                        title = "Overdue",
                        selectedIcon = painterResource(id = R.drawable.ic_overdue_filled),
                        unselectedIcon = painterResource(id = R.drawable.ic_overdue_outlined)
                    ), TabItem (
                        title = "Completed",
                        selectedIcon = painterResource(id = R.drawable.ic_completed_filled),
                        unselectedIcon = painterResource(id = R.drawable.ic_completed_outlined)
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

                TabRow(selectedTabIndex = selectedTabIndex, indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .padding(horizontal = 30.dp)
                            .height(2.dp)
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                }) {

                    listTabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedTabIndex = index
                            },
                            text = {
                                if (index == selectedTabIndex) {
                                    Text(
                                        text = tab.title, color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Text(text = tab.title, color = Color.Gray)
                                }
                            },
                            icon = {
                                Icon(
                                    tint = MaterialTheme.colorScheme.primary,
                                    painter = if (selectedTabIndex == index) {
                                        tab.selectedIcon
                                    } else tab.unselectedIcon,
                                    contentDescription = tab.title
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

        AnimatedVisibility(
            visible = isVisible,
            exit = slideOutVertically(
                targetOffsetY = { it }, animationSpec = tween(durationMillis = 200)
            ),
            enter = slideInVertically(animationSpec = tween(durationMillis = 200),
                initialOffsetY = { it })
        ) {

            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                        if (textTitle.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Enter Title...")
                            }
                        } else if (formattedDate.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Select Due Date")
                            }
                        } else if (formattedTime.isEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Select Due Time")
                            }
                        } else {
                            val task = Task(
                                title = textTitle,
                                description = textDescription,
                                dueDate = formattedDate,
                                dueTime = formattedTime,
                                isCompleted = textIsCompleted
                            )

                            val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)

                            val alarmItem = AlarmItem(
                                LocalDateTime.of(
                                    dueDate.year,
                                    dueDate.month,
                                    dueDate.dayOfMonth,
                                    dueTime.hour,
                                    dueTime.minute
                                ), textTitle, textDescription ?: ""
                            )

                            alarmItem.let(alarmScheduler::schedule)

                            viewModel.addTask(task)
                            navController.popBackStack()
                            isVisible = false

                        }

                    },
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Outlined.Check, contentDescription = "Add Task")
                }
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x99000000))
                        .padding(it)
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isVisible = false
                        }
                        .weight(50f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(50f)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(Color.White)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            OutlinedTextField(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, top = 25.dp),
                                maxLines = 1,
                                value = textTitle,
                                onValueChange = {
                                    textTitle = it
                                },
                                placeholder = {
                                    Text(text = "Do Something")
                                },
                                label = {
                                    Text(
                                        text = "Add Title",
                                        fontFamily = FontFamily(Font(R.font.cereal_med)),
                                        color = Color.Black
                                    )
                                })

                            OutlinedTextField(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                                maxLines = 3,
                                value = textDescription,
                                onValueChange = {
                                    textDescription = it
                                },
                                placeholder = {
                                    Text(text = "Do Something")
                                },
                                label = {
                                    Text(
                                        text = "Add Description (Optional)",
                                        fontFamily = FontFamily(Font(R.font.cereal_lite)),
                                        color = Color.Black
                                    )
                                })

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, top = 25.dp, end = 15.dp)
                            ) {

                                Column {

                                    Text(
                                        text = "Select due date:", fontSize = 12.sp
                                    )

                                    Button(
                                        onClick = {
                                            datePickerState.show()
                                        },
                                        shape = RoundedCornerShape(5.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(
                                            text = if (isDateSelected) {
                                                formattedDate
                                            } else "Due Date",
                                            fontSize = 14.sp,
                                            color = textColorDate
                                        )
                                    }

                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Column {

                                    Text(
                                        text = "Select due time:", fontSize = 12.sp
                                    )

                                    Button(
                                        onClick = {
                                            timePickerState.show()
                                        },
                                        shape = RoundedCornerShape(5.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(
                                            text = if (isTimeSelected) {
                                                formattedTime
                                            } else "Due Time",
                                            fontSize = 14.sp,
                                            color = textColorTime
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
    }

    MaterialDialog(dialogState = datePickerState, buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }) {
        datepicker(
            initialDate = LocalDate.now(), title = "Pick a date"
        ) {
            isDateSelected = true
            dueDate = it
        }
    }

    MaterialDialog(dialogState = timePickerState, buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }) {
        timepicker(
            initialTime = LocalTime.now(), title = "Pick a time"
        ) {
            isTimeSelected = true
            dueTime = it
        }
    }

}


@Composable
fun TaskItem(modifier: Modifier = Modifier, task: Task, repository: Repository) {

    val viewModel: MainViewModel = viewModel(
        factory = MainVMFactory(repository)
    )
    val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
    val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
    val dueDate = LocalDate.parse(task.dueDate, formatter)
    val dueTime = LocalTime.parse(task.dueTime, formatterTime)

    val formattedTodaysDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(LocalDate.now())
        }
    }

    val textColor = if (task.isCompleted) {
        MaterialTheme.colorScheme.primary
    } else if (dueDate.isBefore(LocalDate.now())) {
        Color.Red
    } else if (dueDate.isEqual(LocalDate.now()) && dueTime.isBefore(LocalTime.now())) {
        Color.Red
    } else {
        MaterialTheme.colorScheme.primary
    }

    val textDecoration = if (task.isCompleted) {
        TextDecoration.LineThrough
    } else {
        TextDecoration.None
    }

    val displayDateText = if (task.dueDate == formattedTodaysDate) {
        "Today"
    } else {
        task.dueDate
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
    ) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {

            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(80f)
                        .padding(start = 8.dp, top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = displayDateText,
                        fontSize = 14.sp,
                        color = textColor,
                        fontFamily = FontFamily(Font(R.font.cereal_lite))
                    )

                    Spacer(modifier = Modifier.width(7.5.dp))

                    Text(
                        text = task.dueTime,
                        fontSize = 14.sp,
                        color = textColor,
                        fontFamily = FontFamily(Font(R.font.cereal_lite))
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(10f)
                        .height(30.dp)
                        .padding(top = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Checkbox(checked = task.isCompleted, onCheckedChange = {
                        val newTask = task
                        newTask.isCompleted = it
                        task.isCompleted = it

                        viewModel.updateTask(newTask)

                    })
                }
            }

            Text(
                text = task.title,
                Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                color = Color.Black,
                textDecoration = textDecoration,
                fontFamily = FontFamily(Font(R.font.cereal_bold))
            )

            if (task.description != null) {
                Text(
                    text = task.description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textDecoration = textDecoration,
                    fontFamily = FontFamily(Font(R.font.cereal_lite)),
                    modifier = modifier
                        .padding(start = 15.dp, top = 15.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            if (task.isCompleted) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                ) {
                    Box(modifier = Modifier.weight(90f))
                    Box(
                        modifier = Modifier
                            .weight(10f)
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            tint = Color.Gray,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .clickable {
                                    viewModel.deleteTask(task)
                                },
                            contentDescription = "Delete Task"
                        )
                    }
                }
            }


        }

    }

}

@Preview
@Composable
private fun Preview() {

    TodoAppProjectTheme {
        val repository = Repository(TaskDatabase.getInstance(LocalContext.current).getDao())
        HomeScreen(navController = rememberNavController(), repository = repository)
//        TaskItem(
//            task = Task(
//                0,
//                "This is a task",
//                "And this is the description of this.",
//                "Oct 20 2024",
//                "12:12",
//                false
//            ), repository = repository
//        )
    }

}
