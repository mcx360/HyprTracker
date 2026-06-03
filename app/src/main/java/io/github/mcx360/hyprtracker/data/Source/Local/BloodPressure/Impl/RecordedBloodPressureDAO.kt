package io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordedBloodPressureDAO {
    @Query("SELECT * FROM RecordedBloodPressureReadings ORDER BY date_added DESC, time_added DESC")
    fun getAllBloodPressureReadings(): Flow<List<RecordedBloodPressure>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBloodPressureReading(bloodPressureReading: RecordedBloodPressure)

    @Delete
    suspend fun deleteBloodPressureReading(bloodPressure: RecordedBloodPressure)

    @Query("DELETE FROM RecordedBloodPressureReadings")
    suspend fun deleteAllBloodPressureReadings()

    @Query("""
    SELECT AVG(systolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getSystolicAverage(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MAX(systolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getSystolicMax(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MIN(systolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getSystolicMin(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT AVG(diastolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getDiastolicAverage(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MAX(diastolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getDiastolicMax(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MIN(diastolic_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getDiastolicMin(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT AVG(pulse_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getPulseAverage(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MIN(pulse_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getPulseMin(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT MAX(pulse_value)
    FROM RecordedBloodPressureReadings
    WHERE (:startDate IS NULL OR date_added >= :startDate)
      AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getPulseMax(
        startDate: String? = null,
        endDate: String? = null
    ): Double?

    @Query("""
    SELECT date_added
    FROM RecordedBloodPressureReadings
    ORDER BY date_added ASC
    LIMIT 1
""")
    suspend fun getOldestDate(): String?

    @Query("SELECT EXISTS (SELECT 1 FROM recordedbloodpressurereadings)")
    suspend fun hasRecords() : Boolean

    @Query("""
        SELECT hypertension_stage
        FROM recordedbloodpressurereadings
        WHERE (:startDate IS NULL OR date_added >= :startDate)
          AND (:endDate IS NULL OR date_added <= :endDate)
""")
    suspend fun getStages(startDate: String?, endDate: String?) : List<String>

}