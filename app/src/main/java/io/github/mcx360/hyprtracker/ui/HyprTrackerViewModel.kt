package io.github.mcx360.hyprtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.mcx360.hyprtracker.data.HyprReading
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

class HyprTrackerViewModel : ViewModel() {

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

    fun addReading(reading: HyprReading) {
        _uiState.value = _uiState.value.copy(
            readings = _uiState.value.readings + reading
        )
    }

}