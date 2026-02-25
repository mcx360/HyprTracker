package io.github.mcx360.hyprtracker.ui.test

import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HyprTrackerViewModelUiTest {

    val viewModel = HyprTrackerViewModel()

    @Test
    fun TestSystolicValueUpdates(){
        viewModel.updateSystolicValue("90")
        var currentUiState = viewModel.uiState.value
        assertEquals("90", currentUiState.systolicValue)
    }

    @Test
    fun TestDiastolicValueUpdates(){
        viewModel.updateDiastolicValue("70")
        var currentUiState = viewModel.uiState.value
        assertEquals("70", currentUiState.diastolicValue)
    }

    @Test
    fun TestPulseValueUpdates(){
        viewModel.updatePulseValue("70")
        var currentUiState = viewModel.uiState.value
        assertEquals("70",currentUiState.pulseValue)
    }

    @Test
    fun TestifResetFunctionResetsUIState(){
        viewModel.updateSystolicValue("90")
        viewModel.updateDiastolicValue("70")
        viewModel.updatePulseValue("70")
        viewModel.resetBloodPressureLog()

        var currentUiState = viewModel.uiState.value

        assertEquals(currentUiState.systolicValue, "")
        assertEquals(currentUiState.diastolicValue, "")
        assertEquals(currentUiState.pulseValue, "")
    }
}