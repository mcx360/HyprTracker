package io.github.mcx360.hyprtracker.ui.utils


fun getHyperTensionStage(
    systolicValue: String,
    diastolicValue: String
): String {
    try {
        val diastolicValue = diastolicValue.toInt()
        val systolicValue = systolicValue.toInt()

        return if (systolicValue <= 0 || diastolicValue <= 0) {
            "error"
        } else if (systolicValue >= 160 || diastolicValue >= 100) {
            "Grade 2 Hypertension"
        } else if (systolicValue >= 140 || diastolicValue >= 90) {
            "Grade 1 Hypertension"
        } else if (systolicValue >= 130 || diastolicValue >= 85) {
            "High Normal"
        } else {
            "Normal"
        }

    } catch (_: NumberFormatException) {
        return "error"
    }
}