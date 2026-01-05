package com.emteji.lifepathchild.app.child.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emteji.lifepathchild.app.child.ui.dailypath.DailyPathScreen
import com.emteji.lifepathchild.app.child.ui.focus.TaskFocusScreen
import com.emteji.lifepathchild.app.child.ui.login.LoginScreen

object ChildDestinations {
    const val LOGIN = "login"
    const val DAILY_PATH = "daily_path"
    const val TASK_FOCUS = "task_focus/{taskId}"
}

@Composable
fun ChildAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ChildDestinations.LOGIN) {
        composable(ChildDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(ChildDestinations.DAILY_PATH) {
                        popUpTo(ChildDestinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(ChildDestinations.DAILY_PATH) {
            DailyPathScreen(
                onTaskClick = { taskId ->
                    navController.navigate(ChildDestinations.TASK_FOCUS.replace("{taskId}", taskId))
                }
            )
        }
        
        composable(ChildDestinations.TASK_FOCUS) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskFocusScreen(
                taskId = taskId,
                onBack = { navController.popBackStack() },
                onComplete = {
                    // Navigate back to path on completion
                    navController.popBackStack()
                }
            )
        }
    }
}
