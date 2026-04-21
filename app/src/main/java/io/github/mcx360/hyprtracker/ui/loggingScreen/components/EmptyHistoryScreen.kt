package io.github.mcx360.hyprtracker.ui.loggingScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import kotlin.math.roundToInt

@Composable
fun EmptyHistoryScreen(updateTab: (Int) -> Unit){
    val offset = remember { mutableFloatStateOf(0f) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .offset{ IntOffset(offset.floatValue.roundToInt(), 0)}
                .fillMaxSize()
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offset.floatValue += delta.coerceIn(0f, 300f )
                    },
                    onDragStopped = {
                        if (offset.floatValue > 50){
                            updateTab(0)
                        }
                        offset.floatValue = 0f
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.undraw_add_notes_9xls),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_BP_Log_History_Tab_Title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_BP_Log_History_Tab_Text),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 16.dp))
        }
}