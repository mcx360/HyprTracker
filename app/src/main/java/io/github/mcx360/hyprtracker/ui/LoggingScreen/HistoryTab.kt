package io.github.mcx360.hyprtracker.ui.LoggingScreen

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HistoryTab(hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, updateTab: (Int) -> Unit) {

    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    val listIndexToBeDeleted = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val offset = remember { mutableFloatStateOf(0f) }

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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface).padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Delete_Confirmation_Dialog_Text),
                                textAlign = TextAlign.Center
                            )
                            Row() {
                                Button(
                                    onClick = { showDeleteConfirmationDialog.value = false },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(stringResource(R.string.Cancel_Button_Text))
                                }
                                Button(
                                    onClick = {
                                        showDeleteConfirmationDialog.value = false
                                        //hyprTrackerViewModel.removeReading(index = listIndexToBeDeleted.intValue)
                                        scope.launch {
                                            hyprTrackerViewModel.removeReading(index = listIndexToBeDeleted.intValue)
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
                        offset.floatValue += delta.coerceIn(0f, 300f)
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
                Card(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .testTag(HISTORY_TAB_ITEM),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)
                        Text(hyprTrackerViewModel.formatToRegularDate(hyprTrackerUIState.readings[index].date))
                        Spacer(modifier = Modifier.padding(start = 16.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_analogue_clock),
                            contentDescription = null
                        )
                        Text(hyprTrackerUIState.readings[index].time.substring(0,5))
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(stringResource(R.string.Systolic_Value))
                            Text(hyprTrackerUIState.readings[index].systolicValue)
                            Text("mmHg")
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(stringResource(R.string.Diastolic_Value))
                            Text(hyprTrackerUIState.readings[index].diastolicValue)
                            Text("mmHg")
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(stringResource(R.string.Pulse_Value))
                            if (hyprTrackerUIState.readings[index].pulseValue == "") Text("-") else hyprTrackerUIState.readings[index].pulseValue?.let { Text(it) }
                            Text("BPM")
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
                                stringResource(R.string.Notes_Value) + " " + if (hyprTrackerUIState.readings[index].notes == "") "N/A" else hyprTrackerUIState.readings[index].notes
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