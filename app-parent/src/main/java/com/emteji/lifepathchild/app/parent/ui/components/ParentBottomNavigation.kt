package com.emteji.lifepathchild.app.parent.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoBrown
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoCream
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoGold
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoStone

import androidx.compose.material.icons.filled.Psychology

@Composable
fun ParentBottomNavigation(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = IrokoCream,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == ParentDestinations.DASHBOARD,
            onClick = {
                if (currentRoute != ParentDestinations.DASHBOARD) {
                    navController.navigate(ParentDestinations.DASHBOARD) {
                        popUpTo(ParentDestinations.DASHBOARD) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IrokoBrown,
                selectedTextColor = IrokoBrown,
                indicatorColor = IrokoGold.copy(alpha = 0.2f),
                unselectedIconColor = IrokoStone
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Psychology, contentDescription = "AI") },
            label = { Text("Intelligence") },
            selected = currentRoute == ParentDestinations.INTELLIGENCE,
            onClick = {
                if (currentRoute != ParentDestinations.INTELLIGENCE) {
                    navController.navigate(ParentDestinations.INTELLIGENCE)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IrokoBrown,
                selectedTextColor = IrokoBrown,
                indicatorColor = IrokoGold.copy(alpha = 0.2f),
                unselectedIconColor = IrokoStone
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "Tasks") },
            label = { Text("Tasks") },
            selected = currentRoute == ParentDestinations.TASKS,
            onClick = {
                if (currentRoute != ParentDestinations.TASKS) {
                    navController.navigate(ParentDestinations.TASKS)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IrokoBrown,
                selectedTextColor = IrokoBrown,
                indicatorColor = IrokoGold.copy(alpha = 0.2f),
                unselectedIconColor = IrokoStone
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Lock, contentDescription = "Device") },
            label = { Text("Device") },
            selected = currentRoute == ParentDestinations.DEVICE_CONTROL,
            onClick = {
                if (currentRoute != ParentDestinations.DEVICE_CONTROL) {
                    navController.navigate(ParentDestinations.DEVICE_CONTROL)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = IrokoBrown,
                selectedTextColor = IrokoBrown,
                indicatorColor = IrokoGold.copy(alpha = 0.2f),
                unselectedIconColor = IrokoStone
            )
        )
    }
}
