package io.github.mcx360.hyprtracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalResources
import io.github.mcx360.hyprtracker.ui.mainScreen.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.theme.AppTheme
import io.github.mcx360.hyprtracker.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            AppTheme(darkTheme = darkTheme) {
                if (darkTheme){
                    enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(414141))
                    HyprTrackerScreen(themeViewModel = themeViewModel)
                }
                else{
                    enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(414141, 414141))
                    HyprTrackerScreen(themeViewModel = themeViewModel)
                }
            }
        }
    }
}