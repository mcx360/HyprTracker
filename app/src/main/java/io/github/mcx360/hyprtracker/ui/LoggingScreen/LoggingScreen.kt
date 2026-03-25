package io.github.mcx360.hyprtracker.ui.LoggingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

const val SYSTOLIC_OUTLINEDTEXTFIELD_TAG = "SystolicOutlinedTextField"
const val DIASTOLIC_OUTLINEDTEXTFIELD_TAG = "DiastolicOutlinedTextField"
const val PULSE_OUTLINEDTEXTFIELD_TAG = "PulseOutlinedTextField"
const val CONFIRM_BUTTON_TAG = "confirmButton"
const val LOG_SCREEN_TAB = "LogTab"
const val HISTORY_SCREEN_TAG = "HistoryTab"
const val HISTORY_TAB_ITEM = "HistoryTabItem"

@Composable
fun LoggingScreen(
    modifier: Modifier = Modifier,
    hyprTrackerViewModel: HyprTrackerViewModel,
    snackBarHostState: SnackbarHostState
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoggingScreenTabs(hyprTrackerViewModel, snackBarHostState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingScreenTabs(hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val hyprTackerUiState by hyprTrackerViewModel.uiState.collectAsState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        hyprTrackerViewModel.convertMillisToDate(it)
    } ?: hyprTackerUiState.date
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    var showTimePicker by remember { mutableStateOf(false)
}
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = stringResource(R.string.log_tab)) },
                modifier = Modifier.testTag(LOG_SCREEN_TAB)
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.History_tab)) },
                modifier = Modifier.testTag(HISTORY_SCREEN_TAG)
            )
        }

        when (selectedTab) {
            0 -> LogTab(hyprTrackerViewModel, {updatedValue -> showBottomSheet = true}, snackBarHostState, updateTab = {selectedTab = it})
            1 -> HistoryTab(hyprTrackerViewModel, snackBarHostState) { selectedTab = it }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.Edit_BP_Log_Title),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.titleLarge)

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column() {
                            OutlinedTextField(
                                value = hyprTrackerViewModel.formatToRegularDate(selectedDate),
                                onValueChange = {},
                                label = { Text(stringResource(R.string.Custom_Log_Date_TextField))},
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {showDatePicker = !showDatePicker}) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_date),
                                            contentDescription = null
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                            )

                            OutlinedTextField(
                                value = hyprTackerUiState.time.substring(0,5),
                                onValueChange = {},
                                label = {Text(stringResource(R.string.Custom_Log_Time_TextField))},
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {showTimePicker = !showTimePicker}) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_analogue_clock),
                                            contentDescription = null
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = hyprTackerUiState.notes,
                                onValueChange = {hyprTrackerViewModel.updateNotesValue(it)},
                                label = {Text(stringResource(R.string.Custom_Log_Note_TextField))},
                                maxLines = 1,
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_notes),
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                )
                            )
                        }

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
                                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        TimePicker(
                                            state = timePickerState,
                                        )
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

                        if (showDatePicker) {
                            Popup(
                                onDismissRequest = {showDatePicker = false },
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
                                            showModeToggle = false,
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

                    }

                    Row(horizontalArrangement = Arrangement.Center) {
                        Button(modifier = Modifier.padding(8.dp), onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible){
                                    showBottomSheet = false
                                }

                            }
                        }) {
                            Text("Accept")
                        }

                        Button(modifier = Modifier.padding(8.dp), onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                hyprTrackerViewModel.resetBloodPressureLog()
                                datePickerState.selectedDateMillis = hyprTrackerViewModel.convertDateToMillis(hyprTackerUiState.date)
                                if (!sheetState.isVisible){
                                    showBottomSheet = false
                                }
                            scope.launch {
                                snackBarHostState.showSnackbar("Log entry reset")
                            }
                            }
                        }) {
                            Text(stringResource(R.string.RESET_BP_LOG_EDIT_BUTTON))
                        }
                    }
                }
            }
        }
    }
}