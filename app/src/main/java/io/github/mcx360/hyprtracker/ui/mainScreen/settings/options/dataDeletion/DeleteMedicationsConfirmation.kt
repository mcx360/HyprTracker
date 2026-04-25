package io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.dataDeletion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DeleteMedicationsConfirmation(
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete Medications data?",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(16.dp)
                )
                Text(
                    text = "Doing this will permanently delete all saved medications on your device. Make sure to have backups of any important data",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier.padding(horizontal = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextButton(onClick = { onDismissRequest() }) { Text("Cancel") }
                    TextButton(onClick = {
                        onDismissRequest()
                        onDelete()
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}