package io.github.mcx360.hyprtracker.ui.mainScreen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material3.Card
import io.github.mcx360.hyprtracker.R

@Composable
fun Picker(
    title: String,
    options: Map<String, () -> Unit>,
    onDismissRequest: () -> Unit
) {
    val list = options.keys.toList()

    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card {
            Text(
                text = title,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )

            HorizontalDivider()

            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { (label, action) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Button(onClick = {action()}) {Text(label) }
                    }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.Cancel_Button_Text))
                }
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.Ok))
                }
            }
        }
    }
}