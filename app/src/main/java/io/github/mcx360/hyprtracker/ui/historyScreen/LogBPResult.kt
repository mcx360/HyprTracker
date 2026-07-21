package io.github.mcx360.hyprtracker.ui.historyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.model.HyprReading
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import io.github.mcx360.hyprtracker.ui.utils.convertDateToMillis
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
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(initialHour = currentTime.get(Calendar.HOUR_OF_DAY), initialMinute = currentTime.get(Calendar.MINUTE), is24Hour = true)

    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
            colors = CardColors(
                contentColor = MaterialTheme.colorScheme.surfaceContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContentColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = RectangleShape,
        ){
            TitleBarWithBackButton(title = "Log Blood Pressure") {onDismissRequest() }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)) {
                        Text(
                            text ="Blood Pressure",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(R.drawable.outline_blood_pressure_24),
                            contentDescription = null,
                            tint =MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {

                            //systolic value text field
                            OutlinedTextField(
                                textStyle = MaterialTheme.typography.displayMedium,
                                singleLine = true,
                                value = hyprTackerUiState.systolicValue,
                                onValueChange = {
                                    if (it.isDigitsOnly() && hyprTackerUiState.systolicValue.length <= 3) hyprTrackerViewModel.updateSystolicValue(
                                        it
                                    )
                                },
                                label = { Text(text = stringResource(R.string.systolic), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                shape = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                supportingText = {Text("mmhg", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                            )



                        //diastolic value text field
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.displayMedium,
                            singleLine = true,
                            value = hyprTackerUiState.diastolicValue,
                            onValueChange = {
                                if (it.isDigitsOnly() && hyprTackerUiState.diastolicValue.length <= 3) {
                                    hyprTrackerViewModel.updateDiastolicValue(it)
                                }
                            },
                            label = { Text(text = stringResource(R.string.diastolic), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp, end = 4.dp),
                            supportingText = {Text("mmhg", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                        )

                        //Pulse value text field
                        OutlinedTextField(
                            textStyle = MaterialTheme.typography.displayMedium,
                            singleLine = true,
                            value = hyprTackerUiState.pulseValue,
                            onValueChange = {
                                if (it.isDigitsOnly() && hyprTackerUiState.pulseValue.length <= 3) {
                                    hyprTrackerViewModel.updatePulseValue(it)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            label = { Text(text = stringResource(R.string.pulse), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp),
                            supportingText = {Text("bpm", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                        )
                    }

                    Row() {
                        Text("make sure to measure on the same arm for consistent results.", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp))
                    }
                }

                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp)) {
                        Text("Date and time", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_date),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = formatToRegularDate(selectedDate),
                            onValueChange = {},
                            //label = { Text(stringResource(R.string.Custom_Log_Date_TextField))},
                            readOnly = true,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_date),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {showDatePicker = true}) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = hyprTackerUiState.time.substring(0,5),
                            onValueChange = {},
                            //label = {Text(stringResource(R.string.Custom_Log_Time_TextField))},
                            readOnly = true,
                            leadingIcon = {
                                IconButton(
                                    onClick = {},
                                ){
                                    Icon(
                                        painter = painterResource(R.drawable.ic_analogue_clock),
                                        contentDescription = null)
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {showTimePicker = true}) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text("Press the edit button to change date and time. By default current date and time is used.", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
                }

                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {
                        Text("Notes", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_notes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    OutlinedTextField(
                        value = hyprTackerUiState.notes,
                        onValueChange = {hyprTrackerViewModel.updateNotesValue(it)},
                        label = {Text(stringResource(R.string.Custom_Log_Note_TextField))},
                        minLines = 3,
                        maxLines = 3,
                        trailingIcon = {

                        },
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        placeholder = {Text(stringResource(R.string.Note_placeholder))}
                    )
                    Text("0/100", modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp), textAlign = TextAlign.End)
                    Text("Notes are optional, they can include things such as mood, what arm you used, whether you exercised that day etc.", modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom =8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    val resetMessage = stringResource(R.string.Rest_button_snackBar_message)
                    OutlinedButton(modifier = Modifier.padding(8.dp).weight(1f), onClick = {
                        scope.launch {onDismissRequest()}.invokeOnCompletion {
                            hyprTrackerViewModel.resetBloodPressureLog()
                            datePickerState.selectedDateMillis =
                                convertDateToMillis(hyprTackerUiState.date)
                            haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            scope.launch { snackBarHostState.showSnackbar(resetMessage) }
                        } }
                    ) {
                        Text("Cancel")
                    }
                    Button(onClick = { if (hyprTackerUiState.systolicValue != "" && hyprTackerUiState.diastolicValue != "") {
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
                    }}, modifier = Modifier.weight(1f).padding(16.dp)) {Text("Add Log") }

                }
            }
            //Edit sheet custom date picker
            if (showDatePicker) {
                Popup(
                    onDismissRequest = {showDatePicker = false},
                    alignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ){
                        DatePicker(
                            dateFormatter = DatePickerDefaults.dateFormatter(),
                            state = datePickerState,
                            showModeToggle = false
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                showDatePicker = false
                                hyprTrackerViewModel.updateDateValue(selectedDate)
                            },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(stringResource(R.string.DateSelection_Ok_Button))
                            }
                        }
                    }
                }
            }
            //Edit sheet custom time picker
            if (showTimePicker){
                Popup(
                    onDismissRequest = {showTimePicker = false },
                    alignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TimePicker(state = timePickerState)
                            Button(onClick = {showTimePicker = false}) {
                                Text(stringResource(R.string.Dismiss_TimePicker_Button))
                            }
                            Button(onClick = {

                                hyprTrackerViewModel.updateTimeValue(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute))
                                showTimePicker = false
                            }) {
                                Text(stringResource(R.string.Confirm_TimePicker_Button))
                            }
                        }
                    }
                }
            }
        } }
}