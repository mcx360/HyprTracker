package io.github.mcx360.hyprtracker.data

import android.content.Context
import io.github.mcx360.hyprtracker.data.Source.Local.AppDataBase

interface  AppContainer {
    val bloodPressureRepository: BloodPressureRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val bloodPressureRepository: BloodPressureRepository by lazy {
        OfflineBloodPressureRepository(AppDataBase.getDatabase(context).recordedBloodPressureDAO())
    }
}