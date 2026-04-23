package io.github.mcx360.hyprtracker.ui.loggingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.loggingScreen.components.EmptyHistoryScreen
import io.github.mcx360.hyprtracker.ui.utils.Dot
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun HistoryTab(
    hyprTrackerViewModel: HyprTrackerViewModel,
    snackBarHostState: SnackbarHostState,
    updateTab: (Int) -> Unit
) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    val listIndexToBeDeleted = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val offset = remember { mutableFloatStateOf(0f) }

    if (hyprTrackerUIState.readings.isEmpty()){
        EmptyHistoryScreen {updateTab(0)}
    } else {

        when{
            //Dialog to confirm deletion of history data
            showDeleteConfirmationDialog.value -> {
                Dialog(onDismissRequest = {}) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface).padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Delete_Confirmation_Dialog_Text),
                                textAlign = TextAlign.Center
                            )

                            Row {
                                OutlinedButton(
                                    onClick = { showDeleteConfirmationDialog.value = false },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(stringResource(R.string.Cancel_Button_Text))
                                }
                                Button(
                                    onClick = {
                                        showDeleteConfirmationDialog.value = false
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

        //History list in lazy column
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
                    state = rememberDraggableState { delta -> offset.floatValue += delta.coerceIn(0f, 300f) },
                    onDragStopped = {
                        if (offset.floatValue > 50) updateTab(0)
                        offset.floatValue = 0f
                    }
                )
        ) {

            //each individual entry in history
            items(hyprTrackerUIState.readings.size) { index ->
                Card(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {

                    //Row with date and time
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .testTag(HISTORY_TAB_ITEM),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null, modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            when(hyprTrackerUIState.readings[index].date){
                                LocalDate.now().toString() -> "Today @ "
                                LocalDate.now().minusDays(1).toString() -> "Yesterday @ "
                                LocalDate.now().minusDays(2).toString() -> "Two days ago @ "
                                else -> hyprTrackerViewModel.formatToRegularDate(hyprTrackerUIState.readings[index].date) + " @ "
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_analogue_clock),
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text(
                            text = hyprTrackerUIState.readings[index].time.substring(0,5),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp),)


                    Row(modifier = Modifier.fillMaxWidth()) {

                        //Systolic value
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.Systolic_Value),
                                style = MaterialTheme.typography.headlineSmall,
                                //color = MaterialTheme.colorScheme.secondary
                            )
                            Text(hyprTrackerUIState.readings[index].systolicValue)
                            Text("mmHg", style = MaterialTheme.typography.bodyMedium)
                        }

                        //Diastolic value
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.Diastolic_Value),
                                style = MaterialTheme.typography.headlineSmall,
                                //color = MaterialTheme.colorScheme.secondary
                            )
                            Text(hyprTrackerUIState.readings[index].diastolicValue)
                            Text("mmHg", style = MaterialTheme.typography.bodyMedium)
                        }

                        //Pulse value
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.Pulse_Value),
                                style = MaterialTheme.typography.headlineSmall,
                                //color = MaterialTheme.colorScheme.secondary
                            )
                            if (hyprTrackerUIState.readings[index].pulseValue == "") Text("-") else hyprTrackerUIState.readings[index].pulseValue?.let { Text(it) }
                            Text("bpm", style = MaterialTheme.typography.bodyMedium)
                        }

                        //Category value
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(), horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                stringResource(R.string.Hypertension_Stage_Category),
                                modifier = Modifier.padding(start = 24.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Dot(hyprTrackerUIState.readings[index].stage)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    when (hyprTrackerUIState.readings[index].stage) {
                                        "Normal" -> stringResource(R.string.Normal)
                                        "High Normal" -> stringResource(R.string.High_normal)
                                        "Grade 1 Hypertension" -> stringResource(R.string.Grade1)
                                        "Grade 2 Hypertension" -> stringResource(R.string.Grade2)
                                        else -> stringResource(R.string.Error)
                                    }
                                )
                            }
                        }
                    }

                    Row(modifier = Modifier.padding(16.dp)) {
                        //Notes value
                        Row {
                            Text(
                                text = stringResource(R.string.Notes_Value) + " ",
                                fontWeight = FontWeight.Bold
                            )
                            Text(""+if (hyprTrackerUIState.readings[index].notes == "") "N/A" else hyprTrackerUIState.readings[index].notes)
                        }

                        //Bin Icon
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            FilledTonalIconButton(
                                onClick = {
                                    showDeleteConfirmationDialog.value = true
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