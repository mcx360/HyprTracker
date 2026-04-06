package io.github.mcx360.hyprtracker

import android.app.Application
import io.github.mcx360.hyprtracker.data.AppContainer
import io.github.mcx360.hyprtracker.data.AppDataContainer
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.BloodPressureRepository
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.OfflineBloodPressureRepository

class HyprTrackerApplication : Application() {

    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}