package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel.Medicine
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.DurationCard
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.MedicationScheduleAndDosageCard
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.NotificationsCard
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.medicationInfoCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    modifier: Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    medicineViewModel: MedicineViewModel
){
    val haptic = LocalHapticFeedback.current
    var checked by remember {mutableStateOf(false)}
    var showScheduleDropDownMenu by remember { mutableStateOf(false) }
    var showTimesPerDayDropDownMenu by remember { mutableStateOf(false) }
    var showScheduleInfoDialog by remember { mutableStateOf(false) }
    var showTimesPerDayInfoDialog by remember { mutableStateOf(false) }
    var showDosePerIntakeInfoDialog by remember { mutableStateOf(false) }
    var showSelectSpecifiedNumberOfDaysDialog by remember { mutableStateOf(false) }
    var showDurationDatePicker by remember { mutableStateOf(false) }
    var showSelectedDaysPicker by remember { mutableStateOf(false) }
    val uiState = medicineViewModel.uiState.collectAsState()
    var isMedicationNameFieldInError by remember { mutableStateOf(false) }
    var isMedicationDescriptionFieldInError by remember { mutableStateOf(false) }
    var isMedicationScheduleFieldInError by remember { mutableStateOf(false) }
    var isMedicationTimesPerDayFieldInError by remember { mutableStateOf(false) }
    var isMedicationDosePerIntakeInError by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
        ) {
        Card(
            modifier = modifier.fillMaxSize(),
            colors = CardColors(
                contentColor = MaterialTheme.colorScheme.surfaceContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContentColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = RectangleShape,
        ) {
            Row(modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .systemBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Add Medication",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Medication Info
                medicationInfoCard(
                    medicationName = uiState.value.medicationName,
                    medicationDescription = uiState.value.medicationDescription,
                    isMedicationNameFieldInError = isMedicationNameFieldInError,
                    isMedicationDescriptionFieldInError = isMedicationDescriptionFieldInError,
                    updateMedicationName = {
                        medicineViewModel.updateMedicationName(it)
                        if (uiState.value.medicationName.isNotEmpty()) isMedicationNameFieldInError =
                            false
                    },
                    updateMedicationDescription = {
                        medicineViewModel.updateMedicationDescription(it)
                        if (uiState.value.medicationDescription.isNotEmpty()) isMedicationDescriptionFieldInError =
                            false
                    },
                    setMedicationNameErrorStatusFalse = {
                        isMedicationNameFieldInError = false
                    },
                    setMedicationDescriptionErrorStatusFalse = {
                        isMedicationDescriptionFieldInError = false
                    }
                )

                Spacer(modifier = modifier.height(16.dp))

                //Medication schedule and dosage Card
                MedicationScheduleAndDosageCard(
                    showScheduleDropDownMenu = showScheduleDropDownMenu,
                    showSelectedDaysPicker = showSelectedDaysPicker,
                    showTimesPerDayDropDownMenu = showTimesPerDayDropDownMenu,
                    showDosePerIntakeInfoDialog = showDosePerIntakeInfoDialog,
                    showTimesPerDayInfoDialog = showTimesPerDayInfoDialog,
                    showScheduleInfoDialog = showScheduleInfoDialog,
                    changeScheduleDropDownMenuStatus = { showScheduleDropDownMenu = it },
                    changeShowTimesPerDayDropDownMenuStatus = { showTimesPerDayDropDownMenu = it },
                    changeShowSelectedDaysPickerStatus = { showSelectedDaysPicker = it },
                    changeShowScheduleInfoDialogStatus = { showScheduleInfoDialog = it },
                    changeShowDosePerIntakeDialogStatus = { showDosePerIntakeInfoDialog = it },
                    changeShowTimesPerDayInfoDialogStatus = { showTimesPerDayInfoDialog = it },
                    isMedicationScheduleFieldInError = isMedicationScheduleFieldInError,
                    isMedicationDosePerIntakeInError = isMedicationDosePerIntakeInError,
                    isMedicationTimesPerDayFieldInError = isMedicationTimesPerDayFieldInError,
                    updateMedicationSchedule = { medicineViewModel.updateMedicationSchedule(it) },
                    updateMedicationDose = { medicineViewModel.updateMedicationDose(it) },
                    updateMedicationTimesPerDay = { medicineViewModel.updateMedicationTimesPerDay(it) },
                    addSelectedDay = { medicineViewModel.addSelectedDays(it) },
                    removeSelectedDay = { medicineViewModel.removeSelectedDays(it) },
                    medicationSchedule = uiState.value.medicationSchedule,
                    medicationTimesPerDay = uiState.value.medicationTimesPerDay,
                    medicationDosage = uiState.value.medicationDosage,
                    setIsMedicationScheduleFieldInErrorToFalse = {
                        isMedicationScheduleFieldInError = false
                    },
                    setIsMedicationDosePerIntakeInErrorToFalse = {
                        isMedicationDosePerIntakeInError = false
                    },
                    setIsMedicationTimesPerDayFieldInErrorToFalse = {
                        isMedicationTimesPerDayFieldInError = false
                    }
                )

                Spacer(modifier = modifier.height(16.dp))

                //Notification reminders Card
                NotificationsCard(
                    checked = checked,
                    updateCheckedStatus = { checked = it },
                    updateMedicationNotificationStatus = {
                        medicineViewModel.updateMedicationNotificationStatus(
                            it
                        )
                    },
                    updateMedicationReminderTime = { value, reminder ->
                        medicineViewModel.updateMedicationReminderTime(value, reminder)
                    },
                    medicationSchedule = uiState.value.medicationSchedule,
                    medicationSelectedDays = uiState.value.medicationSelectedDays,
                    medicationTimesPerDay = uiState.value.medicationTimesPerDay,
                    medicationReminderTimes = uiState.value.medicationReminderTimes
                )

                Spacer(modifier = modifier.height(16.dp))

                //Duration Card
                DurationCard(
                    formatToRegularDate = { medicineViewModel.formatToRegularDate(it) },
                    startDate = uiState.value.date,
                    endDate = uiState.value.medicationEndDate,
                    showSelectSpecifiedNumberOfDaysDialog = showSelectSpecifiedNumberOfDaysDialog,
                    showDurationDatePicker = showDurationDatePicker,
                    updateShowDurationDatePicker = { showDurationDatePicker = it },
                    updateShowSelectSpecifiedNumberOfDaysDialog = {
                        showSelectSpecifiedNumberOfDaysDialog = it
                    },
                    updateMedicationEndDateString = { medicineViewModel.updateMedicationEndDate(it) },
                    updateMedicationEndDateLong = {
                        medicineViewModel.updateMedicationEndDate(
                            medicineViewModel.convertMillisToDate(it)
                        )
                    }
                )

                Row(
                    modifier = modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            openAddMedicationScreen.value = !openAddMedicationScreen.value
                            scope.launch {
                                medicineViewModel.resetAddMedication()
                                medicineViewModel.fetchMedications()
                                snackBarHostState.showSnackbar(
                                    "Canceled adding medication",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            when {
                                uiState.value.medicationName.isEmpty() -> isMedicationNameFieldInError =
                                    true
                            }
                            when {
                                uiState.value.medicationDescription.isEmpty() -> isMedicationDescriptionFieldInError =
                                    true
                            }
                            when {
                                uiState.value.medicationSchedule.isEmpty() -> isMedicationScheduleFieldInError =
                                    true
                            }
                            when {
                                uiState.value.medicationTimesPerDay == 0 -> isMedicationTimesPerDayFieldInError =
                                    true
                            }
                            when {
                                uiState.value.medicationDosage.isEmpty() -> isMedicationDosePerIntakeInError =
                                    true
                            }

                            if (isMedicationNameFieldInError || isMedicationDescriptionFieldInError || isMedicationScheduleFieldInError || isMedicationTimesPerDayFieldInError || isMedicationDosePerIntakeInError) {
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                scope.launch {
                                    snackBarHostState.showSnackbar("Invalid data inputted!")
                                }
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                openAddMedicationScreen.value = !openAddMedicationScreen.value
                                scope.launch {
                                    medicineViewModel.fetchMedications()
                                    snackBarHostState.showSnackbar(
                                        message = "Medication added",
                                        duration = SnackbarDuration.Short
                                    )
                                }

                                scope.launch {
                                    medicineViewModel.addMedication(
                                        Medicine(
                                            name = uiState.value.medicationName,
                                            description = uiState.value.medicationDescription,
                                            schedule = uiState.value.medicationSchedule,
                                            scheduledDays = uiState.value.medicationSelectedDays,
                                            timesPerDay = uiState.value.medicationTimesPerDay,
                                            dosePerIntake = uiState.value.medicationDosage,
                                            notificationsEnabled = uiState.value.medicationNotifications,
                                            scheduledNotificationsTime = uiState.value.medicationReminderTimes,
                                            startDate = uiState.value.date,
                                            endDate = uiState.value.medicationEndDate
                                        )
                                    )
                                    medicineViewModel.resetAddMedication()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).padding(16.dp)
                    ) {
                        Text("Add medication")
                    }
                }
            }
        }
    }
}