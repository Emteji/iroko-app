package com.emteji.lifepathchild.core.ui.navigation

sealed class AppDestinations(val route: String) {
    object Login : AppDestinations("login")
    object Dashboard : AppDestinations("dashboard")
    object Settings : AppDestinations("settings")
    // Module specific placeholders
    object ChildHome : AppDestinations("child_home")
    object ParentHome : AppDestinations("parent_home")
    object AdminHome : AppDestinations("admin_home")
}
