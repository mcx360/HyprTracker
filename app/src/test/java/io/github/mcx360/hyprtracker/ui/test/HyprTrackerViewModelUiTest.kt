package io.github.mcx360.hyprtracker.ui.test

import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class HyprTrackerViewModelUiTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


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

    @Test
    fun testCurrentTimeIsShown(){
        var currentUiState = viewModel.uiState.value
        assertEquals(LocalTime.now().withSecond(0).withNano(0).toString(), currentUiState.time)
    }

}