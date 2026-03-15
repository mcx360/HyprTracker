package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.data.HyprReading
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.roundToInt

const val SYSTOLIC_OUTLINEDTEXTFIELD_TAG = "SystolicOutlinedTextField"
const val DIASTOLIC_OUTLINEDTEXTFIELD_TAG = "DiastolicOutlinedTextField"
const val PULSE_OUTLINEDTEXTFIELD_TAG = "PulseOutlinedTextField"
const val CONFIRM_BUTTON_TAG = "confirmButton"
const val LOG_SCREEN_TAB = "Logtab"
const val HISTRORY_SCREEN_TAG = "HistoryTab"
const val HISTORY_COLUMN_TAG = "HistoryColumm"
const val HISTORY_TAB_ITEM = "HistoryTabItem"

@Composable
fun LoggingScreen(
    modifier: Modifier = Modifier,
    hyprTrackerViewModel: HyprTrackerViewModel,
    snackBarHostState: SnackbarHostState
){
    Column(
        modifier = Modifier.fillMaxSize(),
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
    val currentime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentime.get(Calendar.MINUTE),
        is24Hour = true
    )
    var showTimePicker by remember { mutableStateOf(false) }
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
                modifier = Modifier.testTag(HISTRORY_SCREEN_TAG)
            )
        }

        when (selectedTab) {
            0 -> LogTab(hyprTrackerViewModel, {updatedValue -> showBottomSheet = true}, snackBarHostState, updateTab = {selectedTab = it})
            1 -> HistoryTab(hyprTrackerViewModel, snackBarHostState, {selectedTab = it})
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
                                value = selectedDate,
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
                                value = hyprTackerUiState.time,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTab(hyprTrackerViewModel: HyprTrackerViewModel, updateShowBottomSheet: (Boolean) -> Unit, snackBarHostState: SnackbarHostState, updateTab: (Int) -> Unit) {
    val scope = rememberCoroutineScope()
    var offset = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .offset{ IntOffset(offset.floatValue.roundToInt(), 0)}
            .fillMaxSize()
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offset.floatValue += delta.coerceIn(-300f, 0f )
                },
                onDragStopped = {
                    if (offset.floatValue < -50){
                        updateTab(1)
                    }
                    offset.floatValue = 0f
                }
            )
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            val hyprTackerUiState by hyprTrackerViewModel.uiState.collectAsState()
            Text(
                text = stringResource(R.string.Log_BP),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            Row(modifier = Modifier) {

                OutlinedTextField(
                    singleLine = true,
                    value = hyprTackerUiState.systolicValue,
                    onValueChange = {
                        if (it.isDigitsOnly() && hyprTackerUiState.systolicValue.length < 3) {
                            hyprTrackerViewModel.updateSystolicValue(it)
                        }
                    },
                    label = { Text(text = stringResource(R.string.systolic)) },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .testTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG)
                )

                OutlinedTextField(
                    singleLine = true,
                    value = hyprTackerUiState.diastolicValue,
                    onValueChange = {
                        if (it.isDigitsOnly() && hyprTackerUiState.diastolicValue.length < 3) {
                            hyprTrackerViewModel.updateDiastolicValue(it)
                        }
                    },
                    label = { Text(text = stringResource(R.string.diastolic)) },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .testTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG)
                )

                OutlinedTextField(
                    singleLine = true,
                    value = hyprTackerUiState.pulseValue,
                    onValueChange = {
                        if (it.isDigitsOnly() && hyprTackerUiState.pulseValue.length < 3) {
                            hyprTrackerViewModel.updatePulseValue(it)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = stringResource(R.string.pulse)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .testTag(PULSE_OUTLINEDTEXTFIELD_TAG)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_date),
                    contentDescription = null
                )
                Text(hyprTackerUiState.date)
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_analogue_clock),
                    contentDescription = null
                )
                Text(hyprTackerUiState.time)
            }

            Row(modifier = Modifier) {
                Button(
                    onClick = {
                        if (hyprTackerUiState.systolicValue != "" && hyprTackerUiState.diastolicValue != "") {
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
                            hyprTrackerViewModel.resetBloodPressureLog()
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Log entry added!",
                                    duration = SnackbarDuration.Short
                                )
                            }

                        }
                    },
                    modifier = Modifier
                        .weight(2f)
                        .padding(8.dp)
                        .testTag(CONFIRM_BUTTON_TAG)
                ) {
                    Text(text = stringResource(R.string.Confirm_BP_Log))
                    Spacer(modifier = Modifier.padding(4.dp))
                    Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
                }

                Button(
                    onClick = {
                        updateShowBottomSheet(true)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.edit_BP_Log_Details))
                    Spacer(modifier = Modifier.padding(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_document),
                        contentDescription = null
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    stringResource(R.string.BP_stages_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfographicLine(
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    stringArrayResource(R.array.hypertension_subheading),
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Normal_Stage_Colour),
                    stringArrayResource(R.array.hypertension_stage_normal)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Elevated_Stage_Colour),
                    stringArrayResource(R.array.hypertension_stage_elevated)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Stage1_Colour),
                    stringArrayResource(R.array.hypertension_stage_one)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Stage2_Colour),
                    stringArrayResource(R.array.hypertension_stage_two)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_crisis_Colour),
                    stringArrayResource(R.array.hypertension_stage_crisis)
                )
            }
        }
    }
}

@Composable
fun InfographicLine(
    color: Color,
    stage: Array<String>
    ) {

    val textColor = if(color.luminance() > 0.5f) Color.Black else Color.White

    Row(modifier = Modifier
        .padding(vertical = 4.dp, horizontal = 4.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(color = color),
        ) {
            Text(
                text = stage[0],
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                color = textColor
            )
            Text(text = stage[1],
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = stage[2],
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
}

@Composable
fun HistoryTab(hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, updateTab: (Int) -> Unit) {

    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    val listIndexToBeDeleted = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var offset = remember { mutableFloatStateOf(0f) }

    if (hyprTrackerUIState.readings.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .offset{ IntOffset(offset.floatValue.roundToInt(), 0)}
                .fillMaxSize()
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offset.floatValue += delta.coerceIn(0f, 300f )
                    },
                    onDragStopped = {
                        if (offset.floatValue > 50){
                            updateTab(0)

                        }
                        offset.floatValue = 0f
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(R.drawable.undraw_add_notes_9xls),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_BP_Log_History_Tab_Title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_BP_Log_History_Tab_Text),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 16.dp))
        }
    } else {

        when{
            showDeleteConfirmationDialog.value -> {
                Dialog(onDismissRequest = {}) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.background(
                        MaterialTheme.colorScheme.inverseOnSurface)) {
                        Text(text = stringResource(R.string.Delete_Confirmation_Dialog_Text), textAlign = TextAlign.Center)
                        Row() {
                            Button(onClick = {showDeleteConfirmationDialog.value = false},
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(stringResource(R.string.Cancel_Button_Text))
                            }
                            Button(onClick = {
                                showDeleteConfirmationDialog.value = false
                                hyprTrackerViewModel.removeReading(index = listIndexToBeDeleted.intValue)
                                scope.launch {
                                    snackBarHostState.showSnackbar("Log entry removed")
                                }
                            },
                                modifier = Modifier.padding(8.dp)
                                ) {
                                Text(stringResource(R.string.Confirm_Button_Text))
                            }
                        }

                    }
                }
            }
        }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .offset{ IntOffset(offset.floatValue.roundToInt(), 0)}
            .fillMaxSize()
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offset.floatValue += delta
                },
                onDragStopped = {
                    if (offset.floatValue > 50){
                        updateTab(0)
                    }
                    offset.floatValue = 0f
                }
            )
    ) {

        items(hyprTrackerUIState.readings.size,) { index ->
            Card() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .testTag(HISTORY_TAB_ITEM),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)
                    Text(hyprTrackerUIState.readings[index].date)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_analogue_clock),
                        contentDescription = null
                    )
                    Text(hyprTrackerUIState.readings[index].time)

                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings[index].systolicValue)
                        Text(stringResource(R.string.Systolic_Value))
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings[index].diastolicValue)
                        Text(stringResource(R.string.Diastolic_Value))
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings[index].pulseValue)
                        Text(stringResource(R.string.Pulse_Value))
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(), horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Dot(hyprTrackerUIState.readings[index].stage)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when (hyprTrackerUIState.readings[index].stage) {
                                    "Normal" -> stringResource(R.string.Normal)
                                    "Elevated" -> stringResource(R.string.Elevated)
                                    "Stage 1" -> stringResource(R.string.Hypertension_stage_1)
                                    "Stage 2" -> stringResource(R.string.Hypertension_stage_2)
                                    "Hypertension Crisis" -> stringResource(R.string.Hypertension_crisis)
                                    else -> stringResource(R.string.Error)
                                }
                            )
                        }
                        Text(
                            stringResource(R.string.Hypertension_Stage_Category),
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }

                }

                Row(modifier = Modifier.padding(16.dp)) {
                    Column() {
                        Text(
                            stringResource(R.string.Notes_Value) + " " + hyprTrackerUIState.readings[index].notes
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(
                            onClick = { showDeleteConfirmationDialog.value = true
                                        listIndexToBeDeleted.intValue = index
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = null
                            )
                        }
                    }

                }
            }
        }
    }
    }
}

@Composable
fun Dot(
     hyperTensionStage : String
) {
    val colour = when(hyperTensionStage){
        "Normal" -> colorResource(R.color.Hypertension_Normal_Stage_Colour)
        "Elevated" -> colorResource(R.color.Hypertension_Elevated_Stage_Colour)
        "Stage 1" -> colorResource(R.color.Hypertension_Stage1_Colour)
        "Stage 2" -> colorResource(R.color.Hypertension_Stage2_Colour)
        "Hypertension Crisis" -> colorResource(R.color.Hypertension_crisis_Colour)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(colour, shape = CircleShape)
    )
}