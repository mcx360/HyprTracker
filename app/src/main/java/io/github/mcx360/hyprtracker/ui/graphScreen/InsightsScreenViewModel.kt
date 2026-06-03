package io.github.mcx360.hyprtracker.ui.graphScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.mcx360.hyprtracker.HyprTrackerApplication
import io.github.mcx360.hyprtracker.data.Source.Local.BloodPressure.BloodPressureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class InsightsViewModel(private val bloodPressureRepository: BloodPressureRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUIState())
    val uiState: StateFlow<InsightsUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setTimePeriod(LocalDate.now().minusWeeks(1).toString(), LocalDate.now().toString())
            checkRecordsAreAvailable()
        }
    }

    fun setTimePeriod(startDate: String?, endDate: String?){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    systolicAverage = bloodPressureRepository.getSystolicAverage(startDate, endDate).toString(),
                    systolicMax = bloodPressureRepository.getSystolicMax(startDate, endDate).toString(),
                    systolicMin = bloodPressureRepository.getSystolicMin(startDate, endDate).toString(),
                    diastolicAverage = bloodPressureRepository.getDiastolicAverage(startDate, endDate).toString(),
                    diastolicMax = bloodPressureRepository.getDiastolicMax(startDate, endDate).toString(),
                    diastolicMin = bloodPressureRepository.getDiastolicMin(startDate, endDate).toString(),
                    pulseAverage = bloodPressureRepository.getPulseAverage(startDate, endDate).toString(),
                    pulseMax = bloodPressureRepository.getPulseMax(startDate,endDate).toString(),
                    pulseMin = bloodPressureRepository.getPulseMin(startDate,endDate).toString(),
                    startDate = startDate ?: bloodPressureRepository.getOldestDate() ?: LocalDate.now().toString(),
                    endDate = endDate ?: LocalDate.now().toString(),
                )
            }
        }
    }

    fun checkRecordsAreAvailable(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(hasRecords =bloodPressureRepository.hasRecords())
            }
        }
    }

    //Factory
    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return InsightsViewModel(
                    bloodPressureRepository = (application as HyprTrackerApplication).container.bloodPressureRepository,
                ) as T
            }
        }
    }
}

data class InsightsUIState(
    val hasRecords: Boolean = false,
    val startDate: String = LocalDate.now().minusWeeks(1).toString(),
    val endDate: String = LocalDate.now().toString(),
    val systolicAverage: String = "",
    val systolicMax: String = "",
    val systolicMin: String = "",
    val diastolicAverage: String = "",
    val diastolicMax: String = "",
    val diastolicMin: String = "",
    val pulseAverage: String = "",
    val pulseMax: String = "",
    val pulseMin: String = ""
)