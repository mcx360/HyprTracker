package io.github.mcx360.hyprtracker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HyprTrackerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HyprTrackerUIState())
    val uiState: StateFlow<HyprTrackerUIState> = _uiState.asStateFlow()

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

}

