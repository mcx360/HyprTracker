package io.github.mcx360.hyprtracker.ui.mainScreen.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.github.mcx360.hyprtracker.R


@Composable
fun LogScreenMenu(expanded: Boolean, onDismissRequest: () -> Unit, updateOpenSettings: () -> Unit){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {onDismissRequest()}
    ) {
        DropdownMenuItem(
            text = { Text(text = "Settings") },
            leadingIcon = {Icon(painterResource(R.drawable.outline_settings_24), contentDescription =null)},
            onClick = {
                updateOpenSettings()
                onDismissRequest()
            }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(text = "Imports logs") },
            leadingIcon = {Icon(painter = painterResource(R.drawable.outline_file_open_24), contentDescription = null)},
            onClick = {onDismissRequest()}
        )
        DropdownMenuItem(
            text = { Text(text = "Export logs") },
            leadingIcon = {Icon(painter = painterResource(R.drawable.outline_file_export_24), contentDescription = null)},
            onClick = {onDismissRequest()}
        )
    }
}

@Composable
fun smallMenu(expanded: Boolean, onDismissRequest: () -> Unit, updateOpenSettings: () -> Unit){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {onDismissRequest()}
    ) {
        DropdownMenuItem(
            text = { Text(text = "Settings") },
            leadingIcon = {Icon(painterResource(R.drawable.outline_settings_24), contentDescription =null)},
            onClick = {
                updateOpenSettings()
                onDismissRequest()
            }
        )
    }
}