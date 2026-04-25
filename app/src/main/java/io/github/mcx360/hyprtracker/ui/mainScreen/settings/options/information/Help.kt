package io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.information

import android.R
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Help(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
){
    Dialog(onDismissRequest = {onDismissRequest()}){
        Card {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Help", style = MaterialTheme.typography.headlineMedium)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                ) {
                Text("Can I import and export my data?", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
                Text("Yes you can import/export data using the navigation drawers import/export functionality in csv format. Currently only csv is supported.")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Does the app have autobackup?", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
                Text("No, hyprTracker does not have android autobackup enabled as it is an offline only app however you can easily backup you data with the export functionality.")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("How is my data handled?", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)
                Text("The data you input to the app is only on your device and never leaves it unless you explicitly export it.")
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {onDismissRequest()}) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    }
}