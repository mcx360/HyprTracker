package io.github.mcx360.hyprtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.mcx360.hyprtracker.data.BloodPressureRepository
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
import io.github.mcx360.hyprtracker.data.Source.Local.RecordedBloodPressure

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
                            time = LocalTime.now().withSecond(0).withNano(0).toString(),
                            date = LocalDate.now().toString()
                        )
                    }
                }
                delay(1000)
            }
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
                time = LocalTime.now().withSecond(0).withNano(0).toString()
                )

        }
        getCurrentDateAndTime = true
    }

    fun updateSystolicValue(inputtedValue: String){
        _uiState.update { currentState ->
            currentState.copy(systolicValue = inputtedValue)
        }
    }

    fun updateDiastolicValue(inputtedValue: String){
        _uiState.update { currentState ->
            currentState.copy(diastolicValue = inputtedValue)
        }
    }

    fun updatePulseValue(inputtedValue: String){
        _uiState.update { currentState ->
            currentState.copy(pulseValue = inputtedValue)
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

    fun addReading(reading: HyprReading) {
        _uiState.value = _uiState.value.copy(
            readings = _uiState.value.readings + reading
        )
    }

    fun removeReading(index: Int){
        _uiState.value = _uiState.value.copy(
            readings = _uiState.value.readings - _uiState.value.readings[index]
        )
    }

    fun convertMillisToDate(millis: Long?): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        if (millis == null){
            return ""
        } else{
            return formatter.format(Date(millis))
        }
    }

    fun convertDateToMillis(dateString: String?): Long? {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
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




    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
                ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return HyprTrackerViewModel(
                    (application as HyprTrackerApplication).container.bloodPressureRepository
                ) as T
            }
        }
    }
}

data class HyprTrackerUIState(
    val systolicValue: String = "",
    val diastolicValue: String = "",
    val pulseValue: String = "",
    val date: String = LocalDate.now().toString(),
    val time: String = LocalTime.now().withSecond(0).withNano(0).toString(),
    val notes: String = "",
    val readings: List<HyprReading> = listOf()
)

data class HyprReading(
    val systolicValue: String,
    val diastolicValue: String,
    val pulseValue: String?,
    val date: String,
    val time: String,
    val notes: String?,
    val stage: String = getHyperTensionStage(systolicValue, diastolicValue)
)

fun getHyperTensionStage(systolicValue: String, diastolicValue: String) : String {
        try {

            val diastolicValue = diastolicValue.toInt()
            val systolicValue = systolicValue.toInt()

            if (systolicValue <=0 || diastolicValue <=0){
                return "error"
            }
            else if(systolicValue > 180 || diastolicValue >= 120){
                return "Hypertension Crisis"
            }else if (systolicValue >= 140 || diastolicValue >= 90 ){
                return "Stage 2"
            }else if (systolicValue >= 130 || diastolicValue >= 80){
                return "Stage 1"
            }
            else if (systolicValue >= 120 && diastolicValue < 80){
                return "Elevated"
            }
            else if (systolicValue <120 && diastolicValue < 80){
                return "Normal"
            }
            else{
                return "error"
            }
        } catch (e : NumberFormatException){
            return "error"
        }
}
