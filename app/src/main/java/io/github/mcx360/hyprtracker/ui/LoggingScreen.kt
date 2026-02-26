package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.data.HyprReading
import kotlinx.serialization.Contextual

const val SYSTOLIC_OUTLINEDTEXTFIELD_TAG = "SystolicOutlinedTextField"
const val DIASTOLIC_OUTLINEDTEXTFIELD_TAG = "DiastolicOutlinedTextField"
const val PULSE_OUTLINEDTEXTFIELD_TAG = "PulseOutlinedTextField"
const val CONFIRM_BUTTON = "confirmButton"

@Composable
fun LoggingScreen(modifier: Modifier = Modifier,hyprTrackerViewModel: HyprTrackerViewModel){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        LoggingScreenTabs(hyprTrackerViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggingScreenTabs(hyprTrackerViewModel: HyprTrackerViewModel) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = stringResource(R.string.log_tab)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.History_tab)) }
            )
        }

        when (selectedTab) {
            0 -> LogTab(hyprTrackerViewModel)
            1 -> HistoryTab(hyprTrackerViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogTab(hyprTrackerViewModel: HyprTrackerViewModel) {
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
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
            style = MaterialTheme.typography.titleLarge,
            )

        Row(modifier = Modifier) {

            OutlinedTextField(
                singleLine = true,
                value = hyprTackerUiState.systolicValue,
                onValueChange = { hyprTrackerViewModel.updateSystolicValue(it) },
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
                onValueChange = { hyprTrackerViewModel.updateDiastolicValue(it) },
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
                onValueChange = { hyprTrackerViewModel.updatePulseValue(it) },
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

        Row(modifier = Modifier
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
                painter = painterResource(R.drawable.ic_time),
                contentDescription = null
            )
            Text(hyprTackerUiState.time)
        }

        Row(modifier = Modifier) {

                Button(
                onClick = {hyprTrackerViewModel.addReading(
                    HyprReading(
                        systolicValue = hyprTackerUiState.systolicValue,
                        diastolicValue = hyprTackerUiState.diastolicValue,
                        pulseValue = hyprTackerUiState.pulseValue,
                        time = hyprTackerUiState.time,
                        date = hyprTackerUiState.date,
                        notes = "N/A"
                    )
                )
                    hyprTrackerViewModel.resetBloodPressureLog()
                          },

                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
                    .testTag(CONFIRM_BUTTON)
            ) {
                Text(text = stringResource(R.string.Confirm_BP_Log))
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
            }

            Button(
                onClick = {
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = stringResource(R.string.edit_BP_Log_Details))
                Spacer(modifier = Modifier.padding(4.dp))
                Icon(painter = painterResource(R.drawable.ic_document), contentDescription = null)
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
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.BP_stages_title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfographicLine(MaterialTheme.colorScheme.surfaceContainerHighest, stringArrayResource(R.array.hypertension_subheading),)
            InfographicLine(Color(0xFF2E7D32), stringArrayResource(R.array.hypertension_stage_normal))
            InfographicLine(Color(0xFFF9A825), stringArrayResource(R.array.hypertension_stage_elevated))
            InfographicLine(Color(0xFFF57C00), stringArrayResource(R.array.hypertension_stage_one))
            InfographicLine(Color(0xFFD32F2F),stringArrayResource(R.array.hypertension_stage_two))
            InfographicLine(Color(0xFFB71C1C), stringArrayResource(R.array.hypertension_stage_crisis))
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
fun HistoryTab(hyprTrackerViewModel: HyprTrackerViewModel) {
    val hyprTrackerUIState by hyprTrackerViewModel.uiState.collectAsState()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        items(hyprTrackerUIState.readings.size,) { index ->
            Card() {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)
                    Text(hyprTrackerUIState.readings.get(index).date)
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    Icon(painter = painterResource(R.drawable.ic_time), contentDescription = null)
                    Text(hyprTrackerUIState.readings.get(index).time)

                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings.get(index).systolicValue)
                        Text("SYS")
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings.get(index).diastolicValue)
                        Text("DIA")
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hyprTrackerUIState.readings.get(index).pulseValue)
                        Text("BPM")
                    }
                }

                Row(modifier = Modifier.padding(16.dp)) {
                    Text("Notes: " + hyprTrackerUIState.readings.get(index).notes)
                }
            }

        }
    }
}
