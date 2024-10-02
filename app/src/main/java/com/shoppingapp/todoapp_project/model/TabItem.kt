package com.shoppingapp.todoapp_project.model

import androidx.compose.ui.graphics.painter.Painter

data class TabItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter

)