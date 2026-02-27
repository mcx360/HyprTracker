package io.github.mcx360.hyprtracker

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.github.mcx360.hyprtracker.ui.CONFIRM_BUTTON_TAG
import io.github.mcx360.hyprtracker.ui.DIASTOLIC_OUTLINEDTEXTFIELD_TAG
import io.github.mcx360.hyprtracker.ui.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.PULSE_OUTLINEDTEXTFIELD_TAG
import io.github.mcx360.hyprtracker.ui.SYSTOLIC_OUTLINEDTEXTFIELD_TAG
import io.github.mcx360.hyprtracker.ui.theme.HyprTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoggingScreenUiTests() {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setupLoggingScreen(){
        composeTestRule.setContent {
            HyprTrackerTheme {
                HyprTrackerScreen()
            }
        }
    }

    @Test
    fun SystolicValueTextFieldSetsAndUnsets(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("110")
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText("110"))
    }

    @Test
    fun DiastolicValueTextFieldSetsAndUnsets(){
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("70")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText("70"))
    }

    @Test
    fun PulseValueTextFieldSetsAndUnsets(){
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("70")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).assert(hasText("70"))
    }

    @Test
    fun ConfirmButtonClearsAllTextFields(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("110")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("70")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("70")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()

        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
    }
}