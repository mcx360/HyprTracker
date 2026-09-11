package io.github.mcx360.hyprtracker.ui.logsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.CardColors
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.utils.DeletionDialog
import io.github.mcx360.hyprtracker.ui.utils.EmptyScreen
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun LogsScreen(
    hyprTrackerViewModel: HyprTrackerViewModel,
    snackBarHostState: SnackbarHostState,
    openAddBloodPressureLog: MutableState<Boolean>
) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    val listIndexToBeDeleted = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val resource = LocalResources.current

    when {
        openAddBloodPressureLog.value -> {
            LogBPResult(
                onDismissRequest = { openAddBloodPressureLog.value = false },
                hyprTrackerViewModel = hyprTrackerViewModel,
                snackBarHostState = snackBarHostState
            )
        }
    }

    if (hyprTrackerUIState.readings.isEmpty()) {
        EmptyScreen(
            painter = painterResource(R.drawable.undraw_add_notes_9xls),
            heading = stringResource(R.string.Empty_BP_Log_History_Tab_Title),
            subHeading = stringResource(R.string.Empty_BP_Log_History_Tab_Text)
        )
    } else {

        when {
            //Dialog to confirm deletion of history data
            showDeleteConfirmationDialog.value -> {
                DeletionDialog(
                    onDismissRequest = {showDeleteConfirmationDialog.value = false},
                    deletionText = stringResource(R.string.Delete_Confirmation_Dialog_Text),
                    onDeleteRequest = {
                        showDeleteConfirmationDialog.value = false
                        scope.launch {
                            hyprTrackerViewModel.removeReading(index = listIndexToBeDeleted.intValue)
                            snackBarHostState.showSnackbar(resource.getString(R.string.Remove_Button_snackbar_message))
                        }
                    }
                )
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
                OutlinedCard(
                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
                    colors = when(hyprTrackerUIState.readings[index].stage){
                        stringResource(R.string.Normal) -> CardColors(colorResource(R.color.Hypertension_Normal_Stage_Colour), MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                        stringResource(R.string.High_normal) -> CardColors(colorResource(R.color.Hypertension_High_Normal_Stage_Colour), MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                        stringResource(R.string.Grade1) -> CardColors(colorResource(R.color.Hypertension_Grade1_Colour), MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                        stringResource(R.string.Grade2) -> CardColors(colorResource(R.color.Hypertension_Grade2_Colour), MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                        else -> CardColors(Color.DarkGray, MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                    }
                ) {
                    Row {
                        Column(modifier = Modifier.width(8.dp)) {}
                        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerHigh)) {

                            //Row with date and time
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                            ) {

                                Text(
                                    text = when (
                                        hyprTrackerUIState.readings[index].date) {
                                        LocalDate.now().toString() -> stringResource(R.string.Today_at)
                                        LocalDate.now().minusDays(1).toString() -> stringResource(R.string.Yesterday_at)
                                        LocalDate.now().minusDays(2).toString() -> stringResource(R.string.Two_Days_Ago_At)
                                        else -> formatToDayMonthYear(hyprTrackerUIState.readings[index].date)
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(end = 6.dp)
                                )

                                Text(
                                    text = hyprTrackerUIState.readings[index].time.substring(0, 5),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Surface(
                                    color = when (hyprTrackerUIState.readings[index].stage) {
                                        stringResource(R.string.Normal) -> colorResource(R.color.Hypertension_Normal_Stage_Background)
                                        stringResource(R.string.High_normal) -> colorResource(R.color.Hypertension_High_Normal_Stage_Background)
                                        stringResource(R.string.Grade1) -> colorResource(R.color.Hypertension_Grade1_Background)
                                        stringResource(R.string.Grade2) -> colorResource(R.color.Hypertension_Grade2_Background)
                                        else -> MaterialTheme.colorScheme.error
                                    },
                                    shape = RoundedCornerShape(25)
                                ) {
                                    Text(
                                        text = hyprTrackerUIState.readings[index].stage,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = when (hyprTrackerUIState.readings[index].stage) {
                                            stringResource(R.string.Normal) -> colorResource(R.color.Hypertension_Normal_Stage_Colour)
                                            stringResource(R.string.High_normal) -> colorResource(R.color.Hypertension_High_Normal_Stage_Colour)
                                            stringResource(R.string.Grade1) -> colorResource(R.color.Hypertension_Grade1_Colour)
                                            stringResource(R.string.Grade2) -> colorResource(R.color.Hypertension_Grade2_Colour)
                                            else -> MaterialTheme.colorScheme.onError
                                        },
                                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                        textAlign = TextAlign.End,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                            )

                            Row(modifier = Modifier.fillMaxWidth()) {
                                //Systolic value
                                Column(
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.systolic),
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                    Text(
                                        text = hyprTrackerUIState.readings[index].systolicValue,
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                    Text(
                                        text = stringResource(R.string.mmHg),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                //Diastolic value
                                Column(
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.diastolic),
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                    Text(
                                        text = hyprTrackerUIState.readings[index].diastolicValue,
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                    Text(
                                        text = stringResource(R.string.mmHg),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                //Pulse value
                                Column(
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.Pulse_Value),
                                        style = MaterialTheme.typography.labelLarge,
                                    )

                                    if (hyprTrackerUIState.readings[index].pulseValue == "") Text("-") else hyprTrackerUIState.readings[index].pulseValue?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.displaySmall,
                                        )
                                    }
                                    Text(
                                        text = stringResource(R.string.bpm),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                //Notes value
                                Text(
                                    text = if (hyprTrackerUIState.readings[index].notes != "") "" + hyprTrackerUIState.readings[index].notes else stringResource(R.string.No_Notes),
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = FontFamily.SansSerif
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
                                        imageVector = Icons.Filled.MoreHoriz,
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
}