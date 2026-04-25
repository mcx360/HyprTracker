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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import io.github.mcx360.hyprtracker.HyprTrackerApplication
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.Impl.RecordedBloodPressure
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
        bloodPressureRepository.removeBloodPressureReading(reading.systolicValue, reading.diastolicValue, reading.pulseValue, reading.date, reading.time)
    }

    //Helper function that filters readings from current date back up until the cutoff date
    fun getFilteredList(cutoffDate: String): List<HyprReading> {
        val cutoff = LocalDate.parse(cutoffDate)

        return _uiState.value.readings.filter {
            val readingDate = LocalDate.parse(it.date)
            !readingDate.isBefore(cutoff)
        }
    }

    //gets systolic average value from now until the cutoffDate or all time average if no cutoffDate is provided
    fun getSystolicAverage(cutoffDate: String?) : Int{
        try {
            if (cutoffDate == null) {
                return _uiState.value.readings.sumOf { it.systolicValue.toInt() } / _uiState.value.readings.size
            } else {
                val filteredList = getFilteredList(cutoffDate)
                return filteredList.sumOf { it.systolicValue.toInt() } / filteredList.size
            }
        }catch (_: Exception){
            return 0
        }
    }

    //gets systolic minimum value from now until the cutoffDate or all time minimum if no cutoffDate is provided
    fun getSystolicMin(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.minOf {it.systolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).minOf { it.systolicValue.toInt() }
            }
        }catch (_: Exception){
            0
        }
    }

    //gets systolic maximum value from now until the cutoffDate or all time maximum if no cutoffDate is provided
    fun getSystolicMax(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.maxOf {it.systolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).maxOf { it.systolicValue.toInt() }
            }
        }catch (_: ArithmeticException){
            0
        }
    }

    /*
    fun getSystolicValues(cutoffDate: String?): List<Int> {
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings

        return readings.mapNotNull {
            runCatching { it.systolicValue.toInt() }.getOrNull()
        }
    }
     */

    //gets diastolic average value from now until the cutoffDate or all time average if no cutoffDate is provided
    fun getDiastolicAverage(cutoffDate: String?) : Int{
        try {
            if (cutoffDate == null) {
                return _uiState.value.readings.sumOf { it.diastolicValue.toInt() } / _uiState.value.readings.size
            } else {
                val filteredList = getFilteredList(cutoffDate)
                return filteredList.sumOf { it.diastolicValue.toInt() } / filteredList.size
            }
        }catch (_: ArithmeticException){
            return 0
        }
    }

    //gets diastolic minimum value from now until the cutoffDate or all time minimum if no cutoffDate is provided
    fun getDiastolicMin(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.minOf {it.diastolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).minOf { it.diastolicValue.toInt() }
            }
        }catch (_: ArithmeticException){
            0
        }
    }

    //gets diastolic maximum value from now until the cutoffDate or all time maximum if no cutoffDate is provided
    fun getDiastolicMax(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.maxOf {it.diastolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).maxOf { it.diastolicValue.toInt() }
            }
        }catch (_: ArithmeticException){
            0
        }
    }

    /*
    fun getDiastolicValues(cutoffDate: String?): List<Int> {
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings

        return readings.mapNotNull {
            runCatching { it.diastolicValue.toInt() }.getOrNull()
        }
    }
     */

    //gets pulse average value from now until the cutoffDate or all time average if no cutoffDate is provided
    fun getPulseAverage(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.average().takeIf { !it.isNaN() }?.toInt() ?: 0
    }

    //gets pulse minimum value from now until the cutoffDate or all time minimum if no cutoffDate is provided
    fun getPulseMin(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.min()
    }

    //gets pulse maximum value from now until the cutoffDate or all time maximum if no cutoffDate is provided
    fun getPulseMax(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.max()
    }

    /*
    fun getPulseValues(cutoffDate: String?): List<Int> {
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings

        return readings.mapNotNull {
            runCatching { it.pulseValue?.toInt() }.getOrNull()
        }
    }
     */

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

    //Convert date string in format YYYY-MM-DD to millis
    fun convertDateToMillis(dateString: String?): Long? {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return if (dateString.isNullOrEmpty()) {
            null
        } else {
            try {
                formatter.parse(dateString)?.time
            } catch (_: Exception) {
                null
            }
        }
    }

    //Convert time in millis to string in format YYYY-MM-DD
    fun convertMillisToDate(millis: Long?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return if (millis == null) ""  else formatter.format(Date(millis))
    }

    //format date from internal database format YYYY-MM-DD to regular format DD/MM/YYYY
    fun formatToRegularDate(date: String) : String{
        val dateInfo = date.split('-')
        return "${dateInfo[2]}/${dateInfo[1]}/${dateInfo[0]}"
    }

    //Convert blood pressure from UI layer format to data layer format
    fun HyprReading.toRecordedBloodPressure() : RecordedBloodPressure = RecordedBloodPressure(
        id = 0,
        dateAdded = date,
        timeAdded = time,
        systolicValue = systolicValue,
        diastolicValue = diastolicValue,
        pulseValue = pulseValue,
        noteValue = notes,
        hypertensionStage = stage
        )

    //Convert blood pressure from data layer format to ui layer format
    fun RecordedBloodPressure.toHyprReading() : HyprReading = HyprReading(
        date = dateAdded,
        time = timeAdded,
        systolicValue = systolicValue,
        diastolicValue = diastolicValue,
        pulseValue = pulseValue,
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