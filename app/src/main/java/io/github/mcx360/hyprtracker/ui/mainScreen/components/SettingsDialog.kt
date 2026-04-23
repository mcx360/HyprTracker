package io.github.mcx360.hyprtracker.ui.mainScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R

@Composable
fun Settings(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .systemBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text("General", style = MaterialTheme.typography.headlineMedium)
                    Text("Theme", fontWeight = FontWeight.Bold)
                    Text("System Default", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Language", fontWeight = FontWeight.Bold)
                    Text("English (UK)", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Classification table", fontWeight = FontWeight.Bold)
                    Text("Internation society of hypertension", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)

                    Text("Data", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
                    Text("Delete BP data", fontWeight = FontWeight.Bold)
                    Text("Permanently delete all bp data", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Delete medication data", fontWeight = FontWeight.Bold)
                    Text("Permanently delete all medication data", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)

                    Text("About", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
                    Text("Version", fontWeight = FontWeight.Bold)
                    Text("Version 0.5.0", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Help", fontWeight = FontWeight.Bold)
                    Text("FAQ help", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    //Text("3rd party licenses")
                    Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null, modifier = Modifier.padding(vertical = 16.dp))
                    Text("HyprTracker is a mobile application designed to help users conveniently record and track their blood pressure readings. It does not provide medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional regarding any medical concerns or before making decisions about your health.",style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                }
            }
        }
    }
}