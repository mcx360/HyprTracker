package io.github.mcx360.hyprtracker.ui.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@Composable
fun Dot(
    hyperTensionStage : String
) {
    val colour = when(hyperTensionStage){
        "Normal" -> colorResource(R.color.Hypertension_Normal_Stage_Colour)
        "Elevated" -> colorResource(R.color.Hypertension_Elevated_Stage_Colour)
        "Stage 1" -> colorResource(R.color.Hypertension_Stage1_Colour)
        "Stage 2" -> colorResource(R.color.Hypertension_Stage2_Colour)
        "Hypertension Crisis" -> colorResource(R.color.Hypertension_crisis_Colour)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(colour, shape = CircleShape)
    )
}

@Composable
fun DotWithColour(
    colour: Color
) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(colour, shape = CircleShape)
    )
}