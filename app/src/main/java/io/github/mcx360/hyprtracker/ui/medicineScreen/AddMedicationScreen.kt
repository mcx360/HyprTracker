package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.ui.model.Medicine
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onDismissRequest: () -> Unit,
    snackBarHostState:  SnackbarHostState,
    scope: CoroutineScope,
    medicineViewModel: MedicineViewModel
){
    val haptic = LocalHapticFeedback.current
    val uiState = medicineViewModel.uiState.collectAsState()
    val errors = remember { mutableListOf(false, false, false, false) }
    var isMedicationDosePerIntakeInError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Card{
            //Add Medication title
            TitleBarWithBackButton(
                title = "Add Medication",
                onBackArrowClicked = { onDismissRequest(); scope.launch { medicineViewModel.resetAddMedication(); medicineViewModel.fetchMedications() } }
            )

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Medication Info",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(4.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                        )

                        OutlinedTextField(
                            isError = errors[0],
                            onValueChange = { medicineViewModel.updateMedicationName(it); if (uiState.value.medicationName.isNotEmpty()) errors[0] = false },
                            value = uiState.value.medicationName,
                            label = { Text("Medication name*") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                            placeholder = { Text("e.g. Lisinopril") },
                            supportingText = { if (errors[0]) Text(text = "Medication name needed!", color = MaterialTheme.colorScheme.error)  else Text("*required") }
                        )

                        OutlinedTextField(
                            isError = errors[1],
                            onValueChange = { medicineViewModel.updateMedicationDescription(it); if (uiState.value.medicationDescription.isNotEmpty()) errors[1] = false },
                            value = uiState.value.medicationDescription,
                            label = { Text("Medication description*") },
                            maxLines = 1,
                            placeholder = { Text("e.g. Lowers high blood pressure") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                            supportingText = { if (errors[1])  Text(text = "Medication description needed!", color = MaterialTheme.colorScheme.error) else Text("*required")  }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Medication schedule and dosage Card
                MedicationScheduleAndDosageCard(
                    isMedicationScheduleFieldInError = errors[2],
                    isMedicationDosePerIntakeInError = isMedicationDosePerIntakeInError,
                    isMedicationTimesPerDayFieldInError =  errors[3],
                    updateMedicationSchedule = { medicineViewModel.updateMedicationSchedule(it) },
                    updateMedicationDose = { medicineViewModel.updateMedicationDose(it) },
                    updateMedicationTimesPerDay = { medicineViewModel.updateMedicationTimesPerDay(it) },
                    addSelectedDay = { medicineViewModel.addSelectedDays(it) },
                    removeSelectedDay = { medicineViewModel.removeSelectedDays(it) },
                    medicationSchedule = uiState.value.medicationSchedule,
                    medicationTimesPerDay = uiState.value.medicationTimesPerDay,
                    medicationDosage = uiState.value.medicationDosage,
                    setIsMedicationScheduleFieldInErrorToFalse = { errors[2] = false },
                    setIsMedicationDosePerIntakeInErrorToFalse = { isMedicationDosePerIntakeInError = false },
                    setIsMedicationTimesPerDayFieldInErrorToFalse = { errors[3] = false }
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Notification reminders Card
                NotificationsCard(
                    updateMedicationNotificationStatus = { medicineViewModel.updateMedicationNotificationStatus(it) },
                    updateMedicationReminderTime = { value, reminder -> medicineViewModel.updateMedicationReminderTime(value, reminder) },
                    medicationSchedule = uiState.value.medicationSchedule,
                    medicationSelectedDays = uiState.value.medicationSelectedDays,
                    medicationTimesPerDay = uiState.value.medicationTimesPerDay,
                    medicationReminderTimes = uiState.value.medicationReminderTimes
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Duration Card
                DurationCard(
                    startDate = uiState.value.date,
                    endDate = uiState.value.medicationEndDate,
                    updateMedicationEndDate = { medicineViewModel.updateMedicationEndDate(it) },
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        modifier = Modifier.weight(1f).padding(16.dp),
                        onClick = {
                            when {uiState.value.medicationName.isEmpty() -> errors[0] = true }
                            when {uiState.value.medicationDescription.isEmpty() -> errors[1]= true }
                            when {uiState.value.medicationSchedule.isEmpty() ->  errors[2] = true }
                            when {uiState.value.medicationTimesPerDay == 0 ->  errors[3] = true }
                            when {uiState.value.medicationDosage.isEmpty() -> isMedicationDosePerIntakeInError = true }

                            if (errors[0] || errors[1] ||  errors[2] ||  errors[3] || isMedicationDosePerIntakeInError) {
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onDismissRequest()
                                scope.launch {
                                    medicineViewModel.fetchMedications()
                                    snackBarHostState.showSnackbar(
                                        message = "Medication added",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                scope.launch { medicineViewModel.addMedication() }
                            }
                        }
                    ) {
                        Text(text = "Add medication")
                    }
                }
            }
        }
    }
}