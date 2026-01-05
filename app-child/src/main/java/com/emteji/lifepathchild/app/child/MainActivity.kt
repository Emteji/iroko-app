package com.emteji.lifepathchild.app.child

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.emteji.lifepathchild.app.child.navigation.ChildAppNavigation
import com.emteji.lifepathchild.core.ui.theme.LifePathChildTheme // Correct theme import
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        
        setContent {
            LifePathChildTheme {
                ChildAppNavigation()
            }
        }
    }
}
