package com.shoppingapp.todoapp_project.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shoppingapp.todoapp_project.alarm.AlarmItem
import com.shoppingapp.todoapp_project.alarm.AlarmScheduler
import com.shoppingapp.todoapp_project.alarm.AlarmSchedulerImpl
import com.shoppingapp.todoapp_project.model.Task
import com.shoppingapp.todoapp_project.mvvm.MainVMFactory
import com.shoppingapp.todoapp_project.mvvm.MainViewModel
import com.shoppingapp.todoapp_project.mvvm.Repository
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
fun AddTaskScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
    repository: Repository
) {

    var textTitle by remember {
        mutableStateOf("")
    }

    var textDescription by remember {
        mutableStateOf("")
    }

    var textDueDate by remember {
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

    val scope = rememberCoroutineScope()

    val viewModel: MainViewModel = viewModel(
        factory = MainVMFactory(repository)
    )

    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
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
                        ),
                        textTitle,
                        textDescription?:""
                    )

                    alarmItem.let(alarmScheduler::schedule)

                    Toast.makeText(context, "${alarmItem.alarmTime.hour}  ${alarmItem.alarmTime.minute}", Toast.LENGTH_SHORT).show()

                    viewModel.addTask(task)
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Add a Task")
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
            )
        }
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                value = textTitle,
                onValueChange = {
                    textTitle = it
                }, label = {
                    Text(text = "Enter Title")
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 15.dp, end = 15.dp),
                value = textDescription,
                onValueChange = {
                    textDescription = it
                }, label = {
                    Text(text = "Enter Description (Optional)")
                }
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            datePickerState.show()
                        },
                    ) {
                        Text(text = "Select Due Date")
                    }


                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(text = formattedDate, modifier = Modifier.align(Alignment.Center))
                }

            }

            Row() {
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = {
                            timePickerState.show()
                        },
                    ) {
                        Text(text = "Select Due Time")
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = formattedTime)
                }

            }

        }

    }

    MaterialDialog(
        dialogState = timePickerState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker(
            initialTime = LocalTime.NOON,
            title = "Pick a time"
        ) {
            dueTime = it
        }
    }


    MaterialDialog(
        dialogState = datePickerState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date"
        ) {
            dueDate = it
        }
    }


}


@Preview
@Composable
private fun Preview() {
}