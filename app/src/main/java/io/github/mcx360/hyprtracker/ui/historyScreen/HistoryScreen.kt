package io.github.mcx360.hyprtracker.ui.historyScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.loggingScreen.HISTORY_TAB_ITEM
import io.github.mcx360.hyprtracker.ui.utils.Dot
import io.github.mcx360.hyprtracker.ui.utils.EmptyScreen
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun HistoryTab(
    hyprTrackerViewModel: HyprTrackerViewModel,
    snackBarHostState: SnackbarHostState,
    openAddBPlog: MutableState<Boolean>
) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    val listIndexToBeDeleted = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    when{
        openAddBPlog.value -> {
            LogBPResult(
                onDismissRequest = {openAddBPlog.value = false}
            )
        }
    }

    if (hyprTrackerUIState.readings.isEmpty()){
        EmptyScreen(
            painter = painterResource(R.drawable.undraw_add_notes_9xls),
            heading = stringResource(R.string.Empty_BP_Log_History_Tab_Title),
            subHeading = stringResource(R.string.Empty_BP_Log_History_Tab_Text)
            )
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
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            //each individual entry in history
            items(hyprTrackerUIState.readings.size) { index ->
                OutlinedCard(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {
                    Column(modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)) {

                        //Row with date and time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 16.dp)
                                .testTag(HISTORY_TAB_ITEM),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                when (hyprTrackerUIState.readings[index].date) {
                                    LocalDate.now().toString() -> stringResource(R.string.Today_at)
                                    LocalDate.now().minusDays(1)
                                        .toString() -> stringResource(R.string.Yesterday_at)

                                    LocalDate.now().minusDays(2)
                                        .toString() -> stringResource(R.string.Two_Days_Ago_At)

                                    else -> formatToDayMonthYear(hyprTrackerUIState.readings[index].date)
                                },
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Text(
                                text = " "+hyprTrackerUIState.readings[index].time.substring(0, 5),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Text(
                                when (hyprTrackerUIState.readings[index].stage) {
                                    "Normal" -> stringResource(R.string.Normal)
                                    "High Normal" -> stringResource(R.string.High_normal)
                                    "Grade 1 Hypertension" -> stringResource(R.string.Grade1)
                                    "Grade 2 Hypertension" -> stringResource(R.string.Grade2)
                                    else -> stringResource(R.string.Error)
                                }
                            , style =MaterialTheme.typography.titleLarge, color = when(hyprTrackerUIState.readings[index].stage){
                                    "Normal" -> colorResource(R.color.Hypertension_Normal_Stage_Colour)
                                    "High Normal" -> colorResource(R.color.Hypertension_High_Normal_Stage_Colour)
                                    "Grade 1 Hypertension" -> colorResource(R.color.Hypertension_Grade1_Colour)
                                    "Grade 2 Hypertension" -> colorResource(R.color.Hypertension_Grade2_Colour)
                                    else -> Color.Gray
                                }, modifier = Modifier.fillMaxWidth().padding(end = 16.dp), textAlign = TextAlign.End)
                        }

                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp),)


                        Row(modifier = Modifier.fillMaxWidth()) {

                            //Systolic value
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = stringResource(R.string.Systolic_Value),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                                Text(hyprTrackerUIState.readings[index].systolicValue, style = MaterialTheme.typography.displaySmall)
                                Text(text = "mmHg", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            //Diastolic value
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = stringResource(R.string.Diastolic_Value),
                                    style = MaterialTheme.typography.labelLarge,
                                    //color = MaterialTheme.colorScheme.secondary
                                )
                                Text(hyprTrackerUIState.readings[index].diastolicValue,  style = MaterialTheme.typography.displaySmall)
                                Text(text = "mmHg", style = MaterialTheme.typography.labelMedium,  color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            //Pulse value
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = stringResource(R.string.Pulse_Value),
                                    style = MaterialTheme.typography.labelLarge,
                                    //color = MaterialTheme.colorScheme.secondary
                                )
                                if (hyprTrackerUIState.readings[index].pulseValue == "") Text("-") else hyprTrackerUIState.readings[index].pulseValue?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.displaySmall,
                                    )
                                }
                                Text(text = "bpm", style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            //Category value
                            /*
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(), horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    stringResource(R.string.Hypertension_Stage_Category),
                                    modifier = Modifier.padding(start = 24.dp),
                                    style = MaterialTheme.typography.labelLarge,
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
                            */
                        }


                            Row(modifier = Modifier.padding(start = 16.dp, end = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                //Notes value
                                Text(
                                    text = stringResource(R.string.Notes_Value) + " ",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start,
                                )
                                Text(
                                    "" + if (hyprTrackerUIState.readings[index].notes == "") stringResource(
                                        R.string.No_Notes
                                    ) else hyprTrackerUIState.readings[index].notes,
                                    textAlign = TextAlign.Start,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                FilledTonalIconButton(
                                    onClick = {
                                        showDeleteConfirmationDialog.value = true
                                        listIndexToBeDeleted.intValue = index
                                    },
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = null,
                                    )
                                }

                            }

                        }
                    }


                }
            }
        }
    }
