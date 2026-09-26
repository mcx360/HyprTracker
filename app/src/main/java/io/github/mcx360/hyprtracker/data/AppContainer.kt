package io.github.mcx360.hyprtracker.data

import android.content.Context
import io.github.mcx360.hyprtracker.data.source.AppDataBase
import io.github.mcx360.hyprtracker.data.source.bloodPressure.BloodPressureRepository
import io.github.mcx360.hyprtracker.data.source.medication.MedicationRepository

interface  AppContainer {
    val bloodPressureRepository: BloodPressureRepository
    val medicationRepository: MedicationRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val bloodPressureRepository: BloodPressureRepository by lazy {
        BloodPressureRepository(AppDataBase.getDatabase(context).recordedBloodPressureDAO())
    }
    override val medicationRepository: MedicationRepository by lazy {
        MedicationRepository(AppDataBase.getDatabase(context).recordedMedicationDAO())
    }
}