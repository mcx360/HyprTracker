package io.github.mcx360.hyprtracker.ui.settingsScreen.components

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R

@Composable
fun Picker(
    title: String,
    default:String,
    options: Map<String, () -> Unit>,
    onDismissRequest: () -> Unit
) {
    var selectedOption = remember {default}

    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card {
            Text(
                text = title,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )

            HorizontalDivider()

            Column(
                modifier = Modifier.selectableGroup().padding(start = 8.dp),
            ) {
                options.forEach { (label, action) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .selectable(
                                selected = selectedOption == label,
                                onClick = {
                                    action()
                                    selectedOption = label
                                    onDismissRequest()
                                },
                                role = Role.RadioButton
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selectedOption == label, onClick ={
                            action()
                            selectedOption = label
                            onDismissRequest()
                        })
                        Text(label)
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
            }
        }
    }
}

@Composable
fun Title(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
fun Option(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .clickable(onClick = {onClick()})
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}