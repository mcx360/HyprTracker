package io.github.mcx360.hyprtracker.ui.utils

fun getHyperTensionStage(
    systolicValue: String,
    diastolicValue: String
): String {
    try {
        val diastolicValue = diastolicValue.toInt()
        val systolicValue = systolicValue.toInt()

        return if (systolicValue <= 0 || diastolicValue <= 0) {
            "Error"
        } else if (systolicValue >= 160 || diastolicValue >= 100) {
            "Grade 2"
        } else if (systolicValue >= 140 || diastolicValue >= 90) {
            "Grade 1"
        } else if (systolicValue >= 130 || diastolicValue >= 85) {
            "High Normal BP"
        } else {
            "Normal BP"
        }

    } catch (_: NumberFormatException) {
        return "Error"
    }
}