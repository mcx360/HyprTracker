package io.github.mcx360.hyprtracker.ui.loggingScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprReading
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTab(
    hyprTrackerViewModel: HyprTrackerViewModel,
    updateShowBottomSheet: (Boolean) -> Unit,
    snackBarHostState: SnackbarHostState,
    updateTab: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val offset = remember { mutableFloatStateOf(0f) }
    val hyprTackerUiState by hyprTrackerViewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .offset{ IntOffset(offset.floatValue.roundToInt(), 0)}
            .fillMaxSize()
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta -> offset.floatValue += delta.coerceIn(-300f, 0f ) },
                onDragStopped = {
                    if (offset.floatValue < -50) updateTab(1)
                    offset.floatValue = 0f
                }
            )
    ) {
        // Log blood pressure card
        OutlinedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors =  CardDefaults.cardColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            //Log blood pressure title
            Text(
                text = stringResource(R.string.Log_BP),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.titleLarge,
            )

            Row(modifier = Modifier) {
                //systolic value text field
                OutlinedTextField(
                    singleLine = true,
                    value = hyprTackerUiState.systolicValue,
                    onValueChange = { if (it.isDigitsOnly() && hyprTackerUiState.systolicValue.length <= 3)  hyprTrackerViewModel.updateSystolicValue(it)  },
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

                //diastolic value text field
                OutlinedTextField(
                    singleLine = true,
                    value = hyprTackerUiState.diastolicValue,
                    onValueChange = {
                        if (it.isDigitsOnly() && hyprTackerUiState.diastolicValue.length <= 3) {
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

                //Pulse value text field
                OutlinedTextField(
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
                    label = { Text(text = stringResource(R.string.pulse)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .testTag(PULSE_OUTLINEDTEXTFIELD_TAG)

                )
            }

            //Log blood pressure time and date info
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
                Text(hyprTrackerViewModel.formatToRegularDate(hyprTackerUiState.date))
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_analogue_clock),
                    contentDescription = null
                )
                Text(hyprTackerUiState.time.substring(0,5))
            }

            Row(modifier = Modifier) {
                //Confirm button
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
                            }
                            hyprTrackerViewModel.resetBloodPressureLog()
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Log entry added!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        }
                        else{
                            haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Add systolic and diastolic values before logging",
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
                    Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = stringResource(R.string.Confirm_BP_Log))
                }

                //Edit button
                FilledTonalIconButton(
                    onClick = { updateShowBottomSheet(true) },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_document),
                        contentDescription = null
                    )
                }
            }
        }

        //Hypertension classification chart card
        OutlinedCard(
            colors =  CardDefaults.cardColors(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                InfographicLine(
                    MaterialTheme.colorScheme.surface,
                    stringArrayResource(R.array.hypertension_subheading),
                )
                HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                InfographicLine(
                    colorResource(R.color.Hypertension_Normal_Stage_Colour),
                    stringArrayResource(R.array.hypertension_stage_normal)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_High_Normal_Stage_Colour),
                    stringArrayResource(R.array.hypertension_stage_high_normal)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Grade1_Colour),
                    stringArrayResource(R.array.hypertension_stage_Grade1)
                )
                InfographicLine(
                    colorResource(R.color.Hypertension_Grade2_Colour),
                    stringArrayResource(R.array.hypertension_stage_Grade2)
                )
                HorizontalDivider(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                Text(
                    "Source: International Society of Hypertension",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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

    Row(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 8.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DotWithColour(color)
        Text(
            text = stage[0],
            modifier = Modifier
                .weight(2f)
                .padding(4.dp),
        )
        Text(text = stage[1],
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stage[2],
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}