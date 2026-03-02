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
import java.time.LocalTime

class HyprTrackerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HyprTrackerUIState())
    val uiState: StateFlow<HyprTrackerUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            while(isActive){
                _uiState.update { currentState ->
                    currentState.copy(time = LocalTime.now().withSecond(0).withNano(0).toString())
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
                notes = "N/A"
                )
        }
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

    fun addReading(reading: HyprReading) {
        _uiState.value = _uiState.value.copy(
            readings = _uiState.value.readings + reading
        )
    }


}