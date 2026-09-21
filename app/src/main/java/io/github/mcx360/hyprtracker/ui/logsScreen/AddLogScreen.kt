package io.github.mcx360.hyprtracker.ui.logsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.model.HyprReading
import io.github.mcx360.hyprtracker.ui.utils.DurationDatePicker
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogBPResult(
    hyprTrackerViewModel: HyprTrackerViewModel,
    onDismissRequest: () -> Unit,
    snackBarHostState: SnackbarHostState
){
    val scope = rememberCoroutineScope()
    val hyprTackerUiState by hyprTrackerViewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: hyprTackerUiState.date
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(initialHour = currentTime.get(Calendar.HOUR_OF_DAY), initialMinute = currentTime.get(Calendar.MINUTE), is24Hour = true)

    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            colors = CardColors(
                contentColor = MaterialTheme.colorScheme.surfaceContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContentColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer)
        ){
            TitleBarWithBackButton(title = stringResource(R.string.Log_BP)) {
                onDismissRequest()
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)) {
                        Text(
                            text = "Enter Blood Pressure",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {

                        //systolic value text field
                        OutlinedTextField(
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            singleLine = true,
                            value = hyprTackerUiState.systolicValue,
                            onValueChange = {hyprTrackerViewModel.updateSystolicValue(it)},
                            label = {
                                Text(
                                text = stringResource(R.string.systolic),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center)
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center) }
                        )

                        //diastolic value text field
                        OutlinedTextField(
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            singleLine = true,
                            value = hyprTackerUiState.diastolicValue,
                            onValueChange = {hyprTrackerViewModel.updateDiastolicValue(it)},
                            label = {
                                Text(
                                text = stringResource(R.string.diastolic),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center)
                            },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp, end = 4.dp),
                            supportingText = {Text(
                                text = stringResource(R.string.mmHg),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center) }
                        )

                        //Pulse value text field
                        OutlinedTextField(
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            singleLine = true,
                            value = hyprTackerUiState.pulseValue,
                            onValueChange = {hyprTrackerViewModel.updatePulseValue(it)},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            label = { Text(
                                text = stringResource(R.string.pulse),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp),
                            supportingText = {
                                Text(
                                text = stringResource(R.string.bpm),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center)
                            }
                        )
                    }

                    Row {
                        Text(
                            text = "make sure to measure on the same arm for consistent results.",
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end =16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp)) {
                        Text(
                            text = "Date and time",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            //shape = RoundedCornerShape(16.dp),
                            value = formatToRegularDate(selectedDate)+ " "+hyprTackerUiState.time.substring(0,5),
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = null
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {showDatePicker.value = true}) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        text = "Press the edit button to change date and time if desired",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom =8.dp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_notes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 8.dp)

                        )
                        Text(
                            text = "Notes",
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(
                        value = hyprTackerUiState.notes,
                        onValueChange = {hyprTrackerViewModel.updateNotesValue(it)},
                        label = {Text(stringResource(R.string.Custom_Log_Note_TextField))},
                        minLines = 3,
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        placeholder = {Text(text = stringResource(R.string.Note_placeholder))}
                    )
                    Text(
                        text = "0/100",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "Notes are optional, they can include things such as mood, what arm you used, whether you exercised that day etc.",
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom =8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    Row(modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(start = 16.dp, top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint =MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Tips",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Take your blood pressure measurements seated in a chair with your feet on the ground and your arm supported.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
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
                                }
                                hyprTrackerViewModel.resetBloodPressureLog()
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onDismissRequest()
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Log entry added!",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Add systolic and diastolic values before logging",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                    ) {
                        Text(text = "Add Log")
                    }
                }
            }

            if (showDatePicker.value) {
                DurationDatePicker(
                    onDateSelected = {
                        hyprTrackerViewModel.updateDateValue(convertMillisToDate(it))
                        showTimePicker.value = true
                                     },
                    onDismiss = {showDatePicker.value = false}
                )
            }

            if (showTimePicker.value){
                TimePickerDialog(
                    onDismissRequest = {showTimePicker.value = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                hyprTrackerViewModel.updateTimeValue(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
                                showTimePicker.value = false
                            }
                        ) {
                            Text(stringResource(R.string.Confirm_TimePicker_Button))
                        }
                        },
                    title = {Text("Enter date")},
                ){
                    TimePicker(state = timePickerState)
                }
            }
        }
    }
}