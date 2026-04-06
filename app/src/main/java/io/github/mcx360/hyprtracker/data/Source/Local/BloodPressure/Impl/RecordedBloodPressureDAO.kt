package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedBloodPressureDAO {
    @Query("SELECT * FROM RecordedBloodPressureReadings ORDER BY dateAdded DESC, timeAdded DESC")
    fun getAllBloodPressureReadings(): Flow<List<RecordedBloodPressure>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertBloodPressureReading(bloodPressureReading: RecordedBloodPressure)

    @Query(
        """
    DELETE FROM RECORDEDBLOODPRESSUREREADINGS 
    WHERE systolic_value = :systolicValue 
      AND diastolic_value = :diastolicValue 
      AND timeAdded = :time 
      AND dateAdded = :date
      AND (:pulseValue IS NULL AND pulse_value IS NULL OR pulse_value = :pulseValue)
"""
    )
    suspend fun deleteBloodPressureReading(
        systolicValue: String,
        diastolicValue: String,
        pulseValue: String?,
        date: String,
        time: String
    )
}