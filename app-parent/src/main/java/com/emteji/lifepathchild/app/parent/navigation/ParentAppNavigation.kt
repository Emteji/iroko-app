package com.emteji.lifepathchild.app.parent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emteji.lifepathchild.app.parent.ui.splash.SplashScreen
import com.emteji.lifepathchild.app.parent.ui.onboarding.ParentOnboardingScreen
import com.emteji.lifepathchild.app.parent.ui.dashboard.DashboardScreen
import com.emteji.lifepathchild.app.parent.ui.setup.VillageSetupScreen
import com.emteji.lifepathchild.app.parent.ui.setup.VillageSetupViewModel
import com.emteji.lifepathchild.app.parent.ui.link.LinkDeviceScreen
import com.emteji.lifepathchild.app.parent.ui.tasks.TaskGovernanceScreen
import com.emteji.lifepathchild.app.parent.ui.device.DeviceControlScreen
import com.emteji.lifepathchild.app.parent.ui.community.CommunityScreen
import com.emteji.lifepathchild.app.parent.ui.setup.wizard.ChildWizardScreen
import com.emteji.lifepathchild.app.parent.ui.intelligence.IntelligenceScreen

object ParentDestinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val ONBOARDING = "onboarding"
    const val VILLAGE_SETUP = "village_setup"
    const val LINK_DEVICE = "link_device"
    const val DASHBOARD = "dashboard"
    const val TASKS = "tasks"
    const val DEVICE_CONTROL = "device_control"
    const val COMMUNITY = "community"
    const val INTELLIGENCE = "intelligence"
}

@Composable
fun ParentAppNavigation(
    startDestination: String = ParentDestinations.SPLASH,
    targetDestination: String = ParentDestinations.DASHBOARD
) {
    val navController = rememberNavController()
    
    // We share the ViewModel between setup and link screen to pass the ID
    val villageSetupViewModel: VillageSetupViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ParentDestinations.SPLASH) {
            SplashScreen(
                onAnimationFinished = {
                    navController.navigate(targetDestination) {
                        popUpTo(ParentDestinations.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(ParentDestinations.ONBOARDING) {
            ParentOnboardingScreen(
                onOnboardingComplete = {
                    // Navigate to the new Wizard instead of the old Setup
                    navController.navigate(ParentDestinations.VILLAGE_SETUP) {
                        popUpTo(ParentDestinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(ParentDestinations.VILLAGE_SETUP) {
            // Updated to use the new Wizard Screen
            ChildWizardScreen(
                onBack = { navController.popBackStack() },
                onWizardComplete = { childId ->
                    // Pass ID to next screen via savedStateHandle or viewModel sharing if needed
                    // For now, we assume the wizard handled creation
                    navController.navigate(ParentDestinations.LINK_DEVICE)
                }
            )
        }
        composable(ParentDestinations.LINK_DEVICE) {
            val childId by villageSetupViewModel.createdChildId.collectAsState()
            
            LinkDeviceScreen(
                childId = childId ?: "UNKNOWN-ID",
                onContinue = {
                    navController.navigate(ParentDestinations.DASHBOARD) {
                        popUpTo(ParentDestinations.VILLAGE_SETUP) { inclusive = true }
                    }
                }
            )
        }
        composable(ParentDestinations.DASHBOARD) {
            DashboardScreen(
                navController = navController,
                onAddChildClick = {
                    navController.navigate(ParentDestinations.VILLAGE_SETUP)
                }
            )
        }
        composable(ParentDestinations.TASKS) {
            TaskGovernanceScreen(navController = navController)
        }
        composable(ParentDestinations.DEVICE_CONTROL) {
            DeviceControlScreen(navController = navController)
        }
        composable(ParentDestinations.COMMUNITY) {
            CommunityScreen(navController = navController)
        }
        composable(ParentDestinations.INTELLIGENCE) {
            IntelligenceScreen(navController = navController)
        }
    }
}
