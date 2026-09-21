package io.github.mcx360.hyprtracker.ui.logsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.model.HyprReading
import io.github.mcx360.hyprtracker.ui.utils.DurationDatePicker
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogBPResult(
    hyprTrackerViewModel: HyprTrackerViewModel,
    onDismissRequest: () -> Unit,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
){
    val hyprTackerUiState by hyprTrackerViewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: hyprTackerUiState.date
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(initialHour = currentTime.get(Calendar.HOUR_OF_DAY), initialMinute = currentTime.get(Calendar.MINUTE), is24Hour = true)

    Dialog(onDismissRequest = {}) {
        Card(colors = CardColors(contentColor = MaterialTheme.colorScheme.surfaceContainer, containerColor = MaterialTheme.colorScheme.primaryContainer, disabledContentColor = MaterialTheme.colorScheme.surfaceContainer, disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Enter Measurement",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Row {
                OutlinedTextField(
                    value = formatToRegularDate(selectedDate) + " " + hyprTackerUiState.time.substring(0, 5),
                    onValueChange = {},
                    shape = RoundedCornerShape(16.dp),
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            OutlinedTextField(
                value = hyprTackerUiState.notes,
                shape = RoundedCornerShape(16.dp),
                onValueChange = { hyprTrackerViewModel.updateNotesValue(it) },
                label = { Text(stringResource(R.string.Custom_Log_Note_TextField)) },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                placeholder = { Text(text = stringResource(R.string.Note_placeholder)) }
            )

            Row {
                //systolic value text field
                OutlinedTextField(
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true,
                    value = hyprTackerUiState.systolicValue,
                    onValueChange = { hyprTrackerViewModel.updateSystolicValue(it) },
                    label = {
                        Text(
                            text = "Sys",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    supportingText = {
                        Text(
                            text = stringResource(R.string.mmHg),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )

                //diastolic value text field
                OutlinedTextField(
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true,
                    value = hyprTackerUiState.diastolicValue,
                    onValueChange = { hyprTrackerViewModel.updateDiastolicValue(it) },
                    label = {
                        Text(
                            text = "Dia",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, end = 4.dp),
                    supportingText = {
                        Text(
                            text = stringResource(R.string.mmHg),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                //Pulse value text field
                OutlinedTextField(
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true,
                    value = hyprTackerUiState.pulseValue,
                    onValueChange = { hyprTrackerViewModel.updatePulseValue(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(
                            text = "PUL",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    supportingText = {
                        Text(
                            text = stringResource(R.string.bpm),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(onClick = { onDismissRequest() }) {
                    Icon(imageVector = Icons.Filled.Close, null)
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (hyprTackerUiState.systolicValue != "" && hyprTackerUiState.diastolicValue != "") {
                            scope.launch {
                                hyprTrackerViewModel.addReading(
                                    HyprReading(
                                        systolicValue = hyprTackerUiState.systolicValue,
                                        diastolicValue = hyprTackerUiState.diastolicValue,
                                        pulseValue = hyprTackerUiState.pulseValue,
                                        time = hyprTackerUiState.time,
                                        date = hyprTackerUiState.date,
                                        notes = hyprTackerUiState.notes
                                    )
                                )
                                snackBarHostState.showSnackbar("Log entry added!")
                            }
                            hyprTrackerViewModel.resetBloodPressureLog()
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            onDismissRequest()
                        } else {
                            haptic.performHapticFeedback(HapticFeedbackType.Reject)
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Save, null)
                    Text(text = "Save")
                }
            }

            if (showDatePicker.value) {
                DurationDatePicker(
                    onDateSelected = {
                        hyprTrackerViewModel.updateDateValue(convertMillisToDate(it))
                        showTimePicker.value = true
                    },
                    onDismiss = { showDatePicker.value = false }
                )
            }

            if (showTimePicker.value) {
                TimePickerDialog(
                    onDismissRequest = { showTimePicker.value = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                hyprTrackerViewModel.updateTimeValue(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
                                showTimePicker.value = false
                            }
                        ) {
                            Text(text = stringResource(R.string.Confirm_TimePicker_Button))
                        }
                    },
                    title = { Text("Enter date") },
                ) {
                    TimePicker(state = timePickerState)
                }
            }
            }
        }
    }
}