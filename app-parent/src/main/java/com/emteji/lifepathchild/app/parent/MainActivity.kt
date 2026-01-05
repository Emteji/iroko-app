package com.emteji.lifepathchild.app.parent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.emteji.lifepathchild.app.parent.navigation.ParentAppNavigation
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoPlatformTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.emteji.lifepathchild.core.data.repository.UserRepository

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Simple check for now
        val isOnboardingCompleted = false // Should come from repo

        setContent {
            IrokoPlatformTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ParentAppNavigation(
                        targetDestination = if (isOnboardingCompleted) ParentDestinations.DASHBOARD else ParentDestinations.ONBOARDING
                    )
                }
            }
        }
    }
}
