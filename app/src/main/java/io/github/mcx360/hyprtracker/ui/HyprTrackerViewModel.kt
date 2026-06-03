package io.github.mcx360.hyprtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.BloodPressureRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import io.github.mcx360.hyprtracker.HyprTrackerApplication
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
import java.time.format.DateTimeFormatter
import java.util.Collections
import kotlin.String
import kotlin.time.ExperimentalTime

class HyprTrackerViewModel(private val bloodPressureRepository: BloodPressureRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HyprTrackerUIState())
    val uiState: StateFlow<HyprTrackerUIState> = _uiState.asStateFlow()
    var getCurrentDateAndTime = true

    init {
        viewModelScope.launch {
            while(isActive){
                //if getCurrentDateAndTime is set to true then the ui keeps updating date and time to the most current
                if (getCurrentDateAndTime){
                    _uiState.update { currentState ->
                        currentState.copy(
                            time = LocalTime.now().withNano(0).toString(),
                            date = LocalDate.now().toString(),
                        )
                    }
                }
                delay(1000)
            }
        }
        viewModelScope.launch {
            fetchBPReadings()
        }
    }

    //fetches blood pressure readings from database and loads them into uiState
    suspend fun fetchBPReadings(){
        return bloodPressureRepository.getAllRecordingsStream().collect { readings ->
            _uiState.value.readings = readings.map { reading -> reading.toHyprReading() }
        }
    }

    //delete all blood pressure readings in database
    suspend fun deleteAllBPRecords(){
        bloodPressureRepository.removeAllBloodPressureReadings()
    }

    //Resets uiState and enables automatic updating of date and time values
    fun resetBloodPressureLog(){
        _uiState.update { currentState ->
            currentState.copy(
                systolicValue = "",
                diastolicValue = "",
                pulseValue = "",
                notes = "",
                date = LocalDate.now().toString(),
                time = LocalTime.now().withNano(0).toString()
                )
        }
        getCurrentDateAndTime = true
    }

    //Updates systolic value in log tab
    fun updateSystolicValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(systolicValue = inputtedValue)
            }
        }
    }

    //updates diastolic value in log tab
    fun updateDiastolicValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(diastolicValue = inputtedValue)
            }
        }
    }

    //updates pulse value in log tab
    fun updatePulseValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(pulseValue = inputtedValue)
            }
        }
    }

    //updates notes value in log tab
    fun updateNotesValue(inputtedValue: String){
        _uiState.update { currentState ->
            currentState.copy(notes = inputtedValue)
        }
    }

    //Adds custom date value in log tab and stops updating the date
    fun updateDateValue(inputtedValue: String){
        getCurrentDateAndTime = false
        _uiState.update { currentState ->
            currentState.copy(date = inputtedValue)
        }
    }

    //Adds custom time value in log tab and stops updating the time
    fun updateTimeValue(inputtedValue: String){
        getCurrentDateAndTime = false
        _uiState.update { currentState ->
            currentState.copy(time = inputtedValue)
        }
    }

    //Adds a reading to the database
    suspend fun addReading(reading: HyprReading) {
        bloodPressureRepository.addBloodPressureReading(reading.toRecordedBloodPressure())
    }

    //Removes a reading from the database
    suspend fun removeReading(index: Int){
        val reading: HyprReading = _uiState.value.readings[index]
        bloodPressureRepository.removeBloodPressureReading(reading.toRecordedBloodPressure())
    }

    //Helper function that filters readings from current date back up until the cutoff date
    fun getFilteredList(cutoffDate: String): List<HyprReading> {
        val cutoff = LocalDate.parse(cutoffDate)

        return _uiState.value.readings.filter {
            val readingDate = LocalDate.parse(it.date)
            !readingDate.isBefore(cutoff)
        }
    }

    //Get percentage values in a list for pie chart data representation
    fun getBPStagesBreakdown(cutoffDate: String?): List<Float> {
        val counts = mutableListOf(0, 0, 0, 0)
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings

        readings.forEach {
            when (it.stage) {
                "Normal" -> counts[0]++
                "High Normal" -> counts[1]++
                "Grade 1 Hypertension" -> counts[2]++
                "Grade 2 Hypertension" -> counts[3]++
            }
        }

        val total = readings.size.toFloat()

        return if (total > 0) {
            counts.map { (it / total) * 100f }
        } else {
            listOf(0f, 0f, 0f, 0f)
        }
    }

    //helper method for pie chart
    @OptIn(ExperimentalTime::class)
    fun getWeekDaysFromToday() : List<String>{
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun")
        val today = LocalDate.now().dayOfWeek.value-1
        Collections.rotate(daysOfWeek, -today)
        return daysOfWeek
    }


    fun convertStringToDate(dateString: String) : LocalDate{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(dateString, formatter)
        return localDate
    }


    //Convert blood pressure from UI layer format to data layer format
    fun HyprReading.toRecordedBloodPressure() : RecordedBloodPressure = RecordedBloodPressure(
        dateAdded = date,
        timeAdded = time,
        systolicValue = systolicValue.toInt(),
        diastolicValue = diastolicValue.toInt(),
        pulseValue = pulseValue?.toInt(),
        noteValue = notes,
        hypertensionStage = stage
        )

    //Convert blood pressure from data layer format to ui layer format
    fun RecordedBloodPressure.toHyprReading() : HyprReading = HyprReading(
        date = dateAdded,
        time = timeAdded,
        systolicValue = systolicValue.toString(),
        diastolicValue = diastolicValue.toString(),
        pulseValue = pulseValue.toString(),
        notes = noteValue,
        stage = hypertensionStage
    )

    //Factory
    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
                ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HyprTrackerViewModel(
                    bloodPressureRepository = (application as HyprTrackerApplication).container.bloodPressureRepository,
                ) as T
            }
        }
    }
}

data class HyprReading(
    val systolicValue: String,
    val diastolicValue: String,
    val pulseValue: String?,
    val date: String,
    val time: String,
    val notes: String?,
    val stage: String = getHyperTensionStage(systolicValue, diastolicValue)
)

fun getHyperTensionStage(
    systolicValue: String,
    diastolicValue: String
): String {
    try {
        val diastolicValue = diastolicValue.toInt()
        val systolicValue = systolicValue.toInt()

        return if (systolicValue <= 0 || diastolicValue <= 0) {
            "error"
        } else if (systolicValue >= 160 || diastolicValue >= 100) {
            "Grade 2 Hypertension"
        } else if (systolicValue >= 140 || diastolicValue >= 90) {
            "Grade 1 Hypertension"
        } else if (systolicValue >= 130 || diastolicValue >= 85) {
            "High Normal"
        } else {
            "Normal"
        }

    } catch (_: NumberFormatException) {
        return "error"
    }
}