//fake data class to be used for simulating the data layer of the app while I focus on creating the UI layer first, file to be removed
package io.github.mcx360.hyprtracker.data

data class HyprReading(
    val systolicValue: String,
    val diastolicValue: String,
    val pulseValue: String,
    val date: String,
    val time: String,
    val notes: String,
    val stage: String = getHyperTensionStage(systolicValue, diastolicValue)
)

fun getHyperTensionStage(systolicValue: String, diastolicValue: String) : String {
    val diastolicValue = diastolicValue.toInt()
    val systolicValue = systolicValue.toInt()

    if(systolicValue > 180 || diastolicValue >= 120){
        return "Hypertension crisis"
    }else if (systolicValue >= 140 || diastolicValue >= 90 ){
        return "Stage 2"
    }else if (systolicValue >= 130 || diastolicValue >= 80){
        return "Stage 1"
    }
    else if (systolicValue >= 120 && diastolicValue < 80){
        return "Elevated"
    }
    else if (systolicValue <120 && diastolicValue < 80){
        return "Normal"
    } else{
        return "error"
    }
}

object FakeData {
    fun getInitialReadings(): List<HyprReading> {
        return listOf(
            HyprReading("120", "80", "72", "2026-02-20", "08:30", "Morning reading"),
            HyprReading("135", "85", "75", "2026-02-21", "14:15", "After lunch"),
            HyprReading("140", "90", "78", "2026-02-22", "21:00", "Before bed")
        )
    }
}

