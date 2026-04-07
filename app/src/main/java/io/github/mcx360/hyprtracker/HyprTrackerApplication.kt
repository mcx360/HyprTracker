package io.github.mcx360.hyprtracker

import android.app.Application
import io.github.mcx360.hyprtracker.data.AppContainer
import io.github.mcx360.hyprtracker.data.AppDataContainer

class HyprTrackerApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}