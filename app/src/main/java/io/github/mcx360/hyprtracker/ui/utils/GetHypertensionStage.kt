package io.github.mcx360.hyprtracker.ui.utils

import androidx.annotation.StringRes
import io.github.mcx360.hyprtracker.R

fun getHyperTensionStage(
    systolicValue: String,
    diastolicValue: String
): String {
    try {
        val diastolicValue = diastolicValue.toInt()
        val systolicValue = systolicValue.toInt()

        return if (systolicValue <= 0 || diastolicValue <= 0) {
            BloodPressureStages.Error.name
        } else if (systolicValue >= 160 || diastolicValue >= 100) {
            BloodPressureStages.Grade2.name
        } else if (systolicValue >= 140 || diastolicValue >= 90) {
            BloodPressureStages.Grade1.name
        } else if (systolicValue >= 130 || diastolicValue >= 85) {
            BloodPressureStages.HighNormal.name
        } else {
            BloodPressureStages.Normal.name
        }

    } catch (_: NumberFormatException) {
        return BloodPressureStages.Error.name
    }
}

enum class BloodPressureStages(@StringRes val stage: Int){
    Normal(R.string.Normal),
    HighNormal(R.string.High_normal),
    Grade1(R.string.Grade1),
    Grade2(R.string.Grade2),
    Error(R.string.Error)
}