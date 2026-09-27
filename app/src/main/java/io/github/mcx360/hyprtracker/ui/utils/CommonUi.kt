package io.github.mcx360.hyprtracker.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R

@Composable
fun Dot(colour: Color) {
    Box(modifier = Modifier.size(16.dp).background(colour, shape = CircleShape))
}

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    painter: Painter,
    heading: String,
    subHeading: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(0.6f)) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxSize(),
                alignment = Alignment.BottomCenter
            )
        }

        Column(modifier = Modifier.weight(0.4f).fillMaxWidth(), verticalArrangement =Arrangement.Top, horizontalAlignment =Alignment.CenterHorizontally) {
            Text(
                text = heading,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = subHeading,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun TitleBarWithBackButton(
    title: String,
    onBackArrowClicked: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            IconButton(onClick = { onBackArrowClicked() }) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun DeletionDialog(
    onDismissRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    deletionText: String
){
    Dialog(onDismissRequest = onDismissRequest){
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(16.dp)
            ){
                Icon(painter = painterResource(R.drawable.outline_delete_24),null, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = "Permanently delete?", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                Text(text = deletionText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier =Modifier.padding(8.dp).fillMaxWidth()) {
                    TextButton(onClick = {onDismissRequest()}) {Text("Cancel") }
                    FilledTonalButton(onClick = {onDeleteRequest()}) {Text("Delete") }
                }
            }
        }
    }
}

@Composable
fun InfoDialog(
    info: String,
    onDismissRequest: () -> Unit,
    title: String
){
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = info)
                Button(
                    onClick = {onDismissRequest()},
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(stringResource(R.string.Ok))
                }
            }
        }
    }
}

@Composable
fun RangePickerDialog(
    onDismissRequest: () -> Unit,
    onDatesGiven: (Long, Long) -> Unit,
    onFinish: () -> Unit
){
    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ){
        val state = rememberDateRangePickerState()

        Column(verticalArrangement = Arrangement.Top) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DatePickerDefaults.colors().containerColor)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {onDismissRequest()}) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
                TextButton(
                    onClick = {
                        onDatesGiven(state.selectedStartDateMillis ?: 0, state.selectedEndDateMillis ?: 0)
                        onFinish()
                        onDismissRequest()
                    },
                    enabled = state.selectedEndDateMillis != null
                ) {
                    Text(text = stringResource(R.string.Save))
                }
            }
            DateRangePicker(state = state, modifier = Modifier.weight(1f), showModeToggle = false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationDatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateFormatter = DatePickerDefaults.dateFormatter(),
            showModeToggle = false,
        )
    }
}