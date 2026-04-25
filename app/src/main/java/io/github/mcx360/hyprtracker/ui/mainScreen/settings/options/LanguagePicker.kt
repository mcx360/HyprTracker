package io.github.mcx360.hyprtracker.ui.mainScreen.settings.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LanguagePicker(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit
) {
    val radioOptions = listOf("English(UK)")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card {
            Text("Select Language", modifier = modifier.padding(8.dp), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()
            Column(modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            HorizontalDivider()
            Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {onDismissRequest()}) {
                    Text("Cancel")
                }
                TextButton(onClick = {onDismissRequest()}) {
                    Text("Ok")
                }
            }

        }
    }
}
