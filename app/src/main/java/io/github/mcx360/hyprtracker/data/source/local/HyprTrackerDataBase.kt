package io.github.mcx360.hyprtracker.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.impl.RecordedBloodPressure
import io.github.mcx360.hyprtracker.data.source.local.bloodPressure.impl.RecordedBloodPressureDAO
import io.github.mcx360.hyprtracker.data.source.local.medication.impl.RecordedMedication
import io.github.mcx360.hyprtracker.data.source.local.medication.impl.RecordedMedicationDAO

@Database(entities = [RecordedBloodPressure::class, RecordedMedication::class], version = 6, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun recordedBloodPressureDAO() : RecordedBloodPressureDAO

    abstract fun recordedMedicationDAO() : RecordedMedicationDAO

    companion object {
     @Volatile
     private var Instance: AppDataBase? = null

        fun getDatabase(context: Context) : AppDataBase {
            return Instance ?: synchronized(this) {
                        Room.databaseBuilder(context, AppDataBase::class.java, "AppDataBase")
                    }.fallbackToDestructiveMigration(false).build().also { Instance = it }
        }
    }
}