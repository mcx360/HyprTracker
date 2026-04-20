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
import kotlin.String
import kotlin.math.ceil

class HyprTrackerViewModel(private val bloodPressureRepository: BloodPressureRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HyprTrackerUIState())
    val uiState: StateFlow<HyprTrackerUIState> = _uiState.asStateFlow()
    var getCurrentDateAndTime = true

    init {
        viewModelScope.launch {
            while(isActive){
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

    fun updateSystolicValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(systolicValue = inputtedValue)
            }
        }
    }

    fun updateDiastolicValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(diastolicValue = inputtedValue)
            }
        }
    }

    fun updatePulseValue(inputtedValue: String){
        if (inputtedValue.length<4) {
            _uiState.update { currentState ->
                currentState.copy(pulseValue = inputtedValue)
            }
        }
    }

    fun updateNotesValue(inputtedValue: String){
        _uiState.update { currentState ->
            currentState.copy(notes = inputtedValue)
        }
    }

    fun updateDateValue(inputtedValue: String){
        getCurrentDateAndTime = false
        _uiState.update { currentState ->
            currentState.copy(date = inputtedValue)
        }
    }

    fun updateTimeValue(inputtedValue: String){
        getCurrentDateAndTime = false
        _uiState.update { currentState ->
            currentState.copy(time = inputtedValue)
        }
    }

    suspend fun addReading(reading: HyprReading) {
        bloodPressureRepository.addBloodPressureReading(reading.toRecordedBloodPressure())

    }

    suspend fun removeReading(index: Int){
        val reading: HyprReading = _uiState.value.readings[index]
        bloodPressureRepository.removeBloodPressureReading(reading.systolicValue, reading.diastolicValue, reading.pulseValue, reading.date, reading.time)
    }

    suspend fun fetchBPReadings(){
        return bloodPressureRepository.getAllRecordingsStream().collect { readings ->
            _uiState.value.readings = readings.map { reading -> reading.toHyprReading() }
        }
    }

    fun convertMillisToDate(millis: Long?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        if (millis == null){
            return ""
        } else{
            return formatter.format(Date(millis))
        }
    }

    fun getFilteredList(cutoffDate: String): List<HyprReading> {
        val cutoff = LocalDate.parse(cutoffDate)

        return _uiState.value.readings.filter {
            val readingDate = LocalDate.parse(it.date)
            !readingDate.isBefore(cutoff)
        }
    }

    fun getSystolicAverage(cutoffDate: String?) : Int{
        try {
            if (cutoffDate == null) {
                return _uiState.value.readings.sumOf { it.systolicValue.toInt() } / _uiState.value.readings.size
            } else {
                val filteredList = getFilteredList(cutoffDate)
                return filteredList.sumOf { it.systolicValue.toInt() } / filteredList.size
            }
        }catch (e: ArithmeticException){
            return 0
        }
    }

    fun getSystolicMin(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.minOf {it.systolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).minOf { it.systolicValue.toInt() }
            }
        }catch (e: ArithmeticException){
            0
        }
    }

    fun getSystolicMax(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.maxOf {it.systolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).maxOf { it.systolicValue.toInt() }
            }
        }catch (e: ArithmeticException){
            0
        }
    }

    fun getDiastolicAverage(cutoffDate: String?) : Int{
        try {
            if (cutoffDate == null) {
                return _uiState.value.readings.sumOf { it.diastolicValue.toInt() } / _uiState.value.readings.size
            } else {
                val filteredList = getFilteredList(cutoffDate)
                return filteredList.sumOf { it.diastolicValue.toInt() } / filteredList.size
            }
        }catch (e: ArithmeticException){
            return 0
        }
    }

    fun getDiastolicMin(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.minOf {it.diastolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).minOf { it.diastolicValue.toInt() }
            }
        }catch (e: ArithmeticException){
            0
        }
    }

    fun getDiastolicMax(cutoffDate: String?) : Int{
        return try {
            if (cutoffDate == null){
                _uiState.value.readings.maxOf {it.diastolicValue.toInt()}
            } else{
                getFilteredList(cutoffDate).maxOf { it.diastolicValue.toInt() }
            }
        }catch (e: ArithmeticException){
            0
        }
    }

    fun getPulseAverage(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.average().takeIf { !it.isNaN() }?.toInt() ?: 0
    }

    fun getPulseMin(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.min()
    }

    fun getPulseMax(cutoffDate: String?) : Int{
        val readings = cutoffDate?.let { getFilteredList(it) } ?: _uiState.value.readings
        return readings.mapNotNull { it.pulseValue?.toIntOrNull() }.max()
    }

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

    fun convertDateToMillis(dateString: String?): Long? {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return if (dateString.isNullOrEmpty()) {
            null
        } else {
            try {
                formatter.parse(dateString)?.time
            } catch (e: Exception) {
                null
            }
        }
    }

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

    fun RecordedBloodPressure.toHyprReading() : HyprReading = HyprReading(
        date = dateAdded,
        time = timeAdded,
        systolicValue = systolicValue,
        diastolicValue = diastolicValue,
        pulseValue = pulseValue,
        notes = noteValue,
        stage = hypertensionStage
    )

    //format date from internal database format YYYY-MM-DD to regular format DD/MM/YYYY
    fun formatToRegularDate(date: String) : String{
        val dateInfo = date.split('-')
        return "${dateInfo[2]}/${dateInfo[1]}/${dateInfo[0]}"
    }


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

        if (systolicValue <= 0 || diastolicValue <= 0) {
            return "error"
        }
        else if (systolicValue >= 160 || diastolicValue >= 100) {
            return "Grade 2 Hypertension"
        }
        else if (systolicValue >= 140 || diastolicValue >= 90) {
            return "Grade 1 Hypertension"
        }
        else if (systolicValue >= 130 || diastolicValue >= 85) {
            return "High Normal"
        }
        else if (systolicValue < 130 && diastolicValue < 85) {
            return "Normal"
        }
        else {
            return "error"
        }

    } catch (e: NumberFormatException) {
        return "error"
    }
}