package io.github.mcx360.hyprtracker.data.Source.Local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedBloodPressureDAO{
    @Query("SELECT * FROM RecordedBloodPressureReadings")
    fun getAllBloodPressureReadings(): Flow<List<RecordedBloodPressure>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBloodPressureReading(bloodPressureReading: RecordedBloodPressure)

    @Delete
    suspend fun deleteBloodPressureReading(bloodPressureReading: RecordedBloodPressure)
}