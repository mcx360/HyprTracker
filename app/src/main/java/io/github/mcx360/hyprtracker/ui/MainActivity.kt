package io.github.mcx360.hyprtracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.mcx360.hyprtracker.ui.mainScreen.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                HyprTrackerScreen()
            }
        }
    }
}