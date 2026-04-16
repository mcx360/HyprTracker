package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R

@Composable
fun AboutDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            val ctx = LocalContext.current
            val uriHandler = LocalUriHandler.current


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null, modifier = modifier.padding(top = 8.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = " About HyprTracker",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "HyprTracker is an app designed to help you keep a journal of your blood pressure readings.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    )

                Text(
                    text = "Version: 0.5.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp, bottom = 4.dp)
                    ,

                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = {
                        uriHandler.openUri("https://github.com/mcx360/HyprTracker")

                    }) {
                        Icon(painter = painterResource(R.drawable.outline_code_xml_24), contentDescription = null)
                        Text("Source code")
                    }

                    TextButton(onClick = {
                        val subject = Uri.encode("HyprTracker App Feedback")
                        uriHandler.openUri(
                            "mailto:support@app.com?subject=$subject"
                        )
                    }) {
                        Icon(painter = painterResource(R.drawable.outline_mail_24), contentDescription = null)
                        Text("Contact")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()

                    TextButton(
                        onClick = onDismissRequest,
                        modifier = modifier.padding(16.dp).align(Alignment.End)
                    ) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
            }
        }
    }
}