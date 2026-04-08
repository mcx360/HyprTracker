package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.mcx360.hyprtracker.HyprTrackerApplication
import io.github.mcx360.hyprtracker.data.Source.Local.Medication.Impl.RecordedMedication
import io.github.mcx360.hyprtracker.data.Source.Local.Medication.MedicationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class MedicineViewModel(private val medicationRepository: MedicationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineState())
    val uiState: StateFlow<MedicineState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchMedications()
        }
    }

    fun resetAddMedication(){
        _uiState.update { currentState ->
            currentState.copy(
                medicationName = "",
                medicationDescription = "",
                medicationSchedule = "",
                medicationTimesPerDay= 0,
                medicationIntake = 0,
                medicationDosage= "",
                medicationSelectedDays = setOf(),
                medicationReminderTimes = listOf("","","","","","",""),
                medicationEndDate = "",
                medicineList = listOf(),
                medicationNotifications = false,
                date = LocalDate.now().toString()
            )
        }
    }

    fun fetchMedications(){
        viewModelScope.launch(Dispatchers.IO) {
            medicationRepository.getAllMedicationsStream().collect { recordedMedications ->
                _uiState.value.medicineList = recordedMedications.map { recordedMedication -> recordedMedication.toMedicine() }
            }
        }
    }

    fun addMedication(medicine: Medicine){
        viewModelScope.launch(Dispatchers.IO) {
            medicationRepository.addMedication(medicine.toRecordedMedication())
        }
    }

    suspend fun removeMedication(medicine: Medicine){
        medicationRepository.removeMedication(medicine.toRecordedMedication())
    }

    fun updateMedicationName(name: String){
        _uiState.update { currentState ->
            currentState.copy(medicationName = name)
        }
    }

    fun updateMedicationDescription(description: String){
        _uiState.update { currentState ->
            currentState.copy(medicationDescription = description)
        }
    }

    fun updateMedicationSchedule(schedule: String){
        _uiState.update { currentState ->
            currentState.copy(medicationSchedule = schedule)
        }
    }


    fun updateMedicationDose(dose: String){
        _uiState.update { currentState ->
            currentState.copy(medicationDosage = dose)
        }
    }

    fun updateMedicationTimesPerDay(timesPerDay: Int){
        _uiState.update { currentState ->
            currentState.copy(medicationTimesPerDay = timesPerDay)
        }
    }

    fun updateMedicationEndDate(date: String){
        _uiState.update { currentState ->
            currentState.copy(medicationEndDate = date)
        }
    }

    fun updateMedicationNotificationStatus(status: Boolean){
        _uiState.update { currentState ->
            currentState.copy(medicationNotifications = status)
        }
    }

    fun addSelectedDays(vararg days: String){
        val selectedDays = _uiState.value.medicationSelectedDays
        for (day in days){
            _uiState.update { currentState ->
                currentState.copy(medicationSelectedDays = selectedDays + day)
            }
        }
    }

    fun removeSelectedDays(day: String){
        val selectedDays = _uiState.value.medicationSelectedDays
        _uiState.update { currentState ->
            currentState.copy(medicationSelectedDays = selectedDays - day)
        }
    }

    fun updateMedicationReminderTime(inputtedValue: String, reminder: Int){
        val currentReminders = _uiState.value.medicationReminderTimes
        val newReminders = currentReminders.toMutableList().apply {
            this[reminder] = inputtedValue
        }
        _uiState.update { currentState ->
            currentState.copy(medicationReminderTimes = newReminders )
        }
    }

    //format date from internal database format YYYY-MM-DD to regular format DD/MM/YYYY
    fun formatToRegularDate(date: String) : String{
        val dateInfo = date.split('-')
        return "${dateInfo[2]}/${dateInfo[1]}/${dateInfo[0]}"
    }

    data class Medicine(
        val name: String,
        val description: String,
        val schedule: String,
        val scheduledDays: Set<String>,
        val timesPerDay: Int,
        val dosePerIntake: String,
        val notificationsEnabled: Boolean,
        val scheduledNotificationsTime: List<String>,
        val startDate: String,
        val endDate: String
    )

    fun RecordedMedication.toMedicine() : Medicine = Medicine(
        name = name,
        description = description,
        schedule = schedule,
        timesPerDay = timesPerDay,
        dosePerIntake = dosePerIntake,
        notificationsEnabled = notificationsEnabled,
        scheduledNotificationsTime = reminders.split(","),
        scheduledDays = schedule.split(",").toSet(),
        startDate = startDate,
        endDate = endDate
    )

    fun Medicine.toRecordedMedication() : RecordedMedication = RecordedMedication(
        id = 0,
        name = name,
        description = description,
        schedule = schedule,
        timesPerDay = timesPerDay,
        dosePerIntake = dosePerIntake,
        scheduledDays = scheduledDays.toString(),
        notificationsEnabled = notificationsEnabled,
        reminders = scheduledNotificationsTime.toString(),
        startDate = startDate,
        endDate = endDate
    )

    fun convertMillisToDate(millis: Long?): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        if (millis == null){
            return ""
        } else{
            return formatter.format(Date(millis))
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MedicineViewModel(
                    medicationRepository = (application as HyprTrackerApplication).container.medicationRepository,
                ) as T
            }
        }
    }
}