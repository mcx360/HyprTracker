package io.github.mcx360.hyprtracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.mcx360.hyprtracker.ui.mainScreen.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.mainScreen.components.settings.options.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.theme.AppTheme
import io.github.mcx360.hyprtracker.ui.theme.ThemeMode

var darkTheme by mutableStateOf<Boolean?>(null)

class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            AppTheme(darkTheme = darkTheme) {
                HyprTrackerScreen(themeViewModel = themeViewModel)
            }
        }
    }
}