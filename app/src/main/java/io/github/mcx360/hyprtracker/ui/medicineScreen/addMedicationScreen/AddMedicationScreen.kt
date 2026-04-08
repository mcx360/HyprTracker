package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel.Medicine
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.DurationDatePicker
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.SelectSpecifiedNumberOfDaysDialog
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.MedicationScheduleAndDosageCard
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.NotificationsCard
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components.medicationInfoCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

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
    val radioButtons = listOf("Continuous", "Specified number of days", "Until a selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }
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


    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
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
                if (uiState.value.medicationName.isNotEmpty()) isMedicationNameFieldInError = false
            },
            updateMedicationDescription = {
                medicineViewModel.updateMedicationDescription(it)
                if (uiState.value.medicationDescription.isNotEmpty()) isMedicationDescriptionFieldInError = false
            },
            setMedicationNameErrorStatusFalse = {
                isMedicationNameFieldInError = false
            },
            setMedicationDescriptionErrorStatusFalse = {
                isMedicationDescriptionFieldInError = false
            }
        )

        Spacer(modifier = modifier.height(16.dp))

        //Medication schedule and dosage
        MedicationScheduleAndDosageCard(
            showScheduleDropDownMenu = showScheduleDropDownMenu,
            showSelectedDaysPicker = showSelectedDaysPicker,
            showTimesPerDayDropDownMenu = showTimesPerDayDropDownMenu,
            showDosePerIntakeInfoDialog = showDosePerIntakeInfoDialog,
            showTimesPerDayInfoDialog = showTimesPerDayInfoDialog,
            showScheduleInfoDialog = showScheduleInfoDialog,
            changeScheduleDropDownMenuStatus = {showScheduleDropDownMenu = it},
            changeShowTimesPerDayDropDownMenuStatus = {showTimesPerDayDropDownMenu = it},
            changeShowSelectedDaysPickerStatus = {showSelectedDaysPicker = it},
            changeShowScheduleInfoDialogStatus = {showScheduleInfoDialog = it},
            changeShowDosePerIntakeDialogStatus = {showDosePerIntakeInfoDialog = it},
            changeShowTimesPerDayInfoDialogStatus = {showTimesPerDayInfoDialog = it},
            isMedicationScheduleFieldInError = isMedicationScheduleFieldInError,
            isMedicationDosePerIntakeInError = isMedicationDosePerIntakeInError,
            isMedicationTimesPerDayFieldInError = isMedicationTimesPerDayFieldInError,
            updateMedicationSchedule = {medicineViewModel.updateMedicationSchedule(it)},
            updateMedicationDose = {medicineViewModel.updateMedicationDose(it)},
            updateMedicationTimesPerDay = {medicineViewModel.updateMedicationTimesPerDay(it)},
            addSelectedDay = {medicineViewModel.addSelectedDays(it)},
            removeSelectedDay = {medicineViewModel.removeSelectedDays(it)},
            medicationSchedule = uiState.value.medicationSchedule,
            medicationTimesPerDay = uiState.value.medicationTimesPerDay,
            medicationDosage = uiState.value.medicationDosage,
            setIsMedicationScheduleFieldInErrorToFalse = {isMedicationScheduleFieldInError = false},
            setIsMedicationDosePerIntakeInErrorToFalse = {isMedicationDosePerIntakeInError = false},
            setIsMedicationTimesPerDayFieldInErrorToFalse = {isMedicationTimesPerDayFieldInError = false}
        )

        Spacer(modifier = modifier.height(16.dp))

        //Notification reminders
        NotificationsCard(
            checked = checked,
            updateCheckedStatus = {checked = it},
            updateMedicationNotificationStatus = {medicineViewModel.updateMedicationNotificationStatus(it)},
            updateMedicationReminderTime = {value, reminder ->
                medicineViewModel.updateMedicationReminderTime(value, reminder)},
            medicationSchedule = uiState.value.medicationSchedule,
            medicationSelectedDays = uiState.value.medicationSelectedDays,
            medicationTimesPerDay = uiState.value.medicationTimesPerDay,
            medicationReminderTimes = uiState.value.medicationReminderTimes
        )

        Spacer(modifier = modifier.height(16.dp))

        //Duration
        Card {
            Text(
                text = "Duration",
                modifier = modifier.padding(start = 16.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
                )

            Text("Start date: "+ medicineViewModel.formatToRegularDate(uiState.value.date), modifier = modifier.padding(start = 16.dp))

            Column(modifier.selectableGroup()) {
                radioButtons.forEach { text ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text)
                                    when(text){
                                        "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog = true
                                        "Until a selected date" -> showDurationDatePicker = true
                                    }
                                          },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (text==selectedOption), onClick = {
                            onOptionSelected(text)
                            when(text){
                                "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog = true
                                "Until a selected date" -> showDurationDatePicker = true
                            }
                        })
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.padding(start = 16.dp)
                        )
                    }
                }
                if (selectedOption == "Specified number of days"){
                    if (uiState.value.medicationEndDate != ""){
                        Text("medicine recorded until "+medicineViewModel.formatToRegularDate(uiState.value.medicationEndDate), modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                    }
                } else if(selectedOption == "Until a selected date"){
                    if (uiState.value.medicationEndDate != ""){
                         Text(medicineViewModel.formatToRegularDate(uiState.value.medicationEndDate), modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                    }
                }else{
                    Text("Medicine will be recorded indefinitely unless cancelled by the user", modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                }
            }
            if (showSelectSpecifiedNumberOfDaysDialog) {
                SelectSpecifiedNumberOfDaysDialog(onDismissRequest = {
                    showSelectSpecifiedNumberOfDaysDialog = false
                }, onNumOfDaysSelected = {
                    if (it != "") {
                        medicineViewModel.updateMedicationEndDate(LocalDate.now().plusDays(it.toLong()).toString())
                    }
                }
                    )
            }
            if (showDurationDatePicker) {
                DurationDatePicker(onDateSelected = { medicineViewModel.updateMedicationEndDate(medicineViewModel.convertMillisToDate(it))}, onDismiss = {showDurationDatePicker = false})
            }
        }

        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                    scope.launch {
                        medicineViewModel.resetAddMedication()
                        snackBarHostState.showSnackbar("Canceled adding medication", duration = SnackbarDuration.Short)
                        openAddMedicationScreen.value = !openAddMedicationScreen.value

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
                        uiState.value.medicationName.isEmpty() -> isMedicationNameFieldInError = true
                    }
                    when {
                        uiState.value.medicationDescription.isEmpty() -> isMedicationDescriptionFieldInError = true
                    }
                    when {
                        uiState.value.medicationSchedule.isEmpty() -> isMedicationScheduleFieldInError = true
                    }
                    when {
                        uiState.value.medicationTimesPerDay == 0 -> isMedicationTimesPerDayFieldInError = true
                    }
                    when{
                        uiState.value.medicationDosage.isEmpty() -> isMedicationDosePerIntakeInError = true
                    }

                    if (isMedicationNameFieldInError || isMedicationDescriptionFieldInError || isMedicationScheduleFieldInError || isMedicationTimesPerDayFieldInError || isMedicationDosePerIntakeInError){
                        haptic.performHapticFeedback(HapticFeedbackType.Reject)
                        scope.launch {
                            snackBarHostState.showSnackbar("Invalid data inputted!")
                        }
                    }
                    else{
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Medication added",
                                duration = SnackbarDuration.Short
                            )
                            openAddMedicationScreen.value = !openAddMedicationScreen.value
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