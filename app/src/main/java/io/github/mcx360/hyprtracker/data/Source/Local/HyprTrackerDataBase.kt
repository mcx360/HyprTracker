package io.github.mcx360.hyprtracker.data.Source.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecordedBloodPressure::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun recordedBloodPressureDAO() : RecordedBloodPressureDAO

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