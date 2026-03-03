package io.github.mcx360.hyprtracker

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.github.mcx360.hyprtracker.ui.CONFIRM_BUTTON_TAG
import io.github.mcx360.hyprtracker.ui.DIASTOLIC_OUTLINEDTEXTFIELD_TAG
import io.github.mcx360.hyprtracker.ui.HISTORY_TAB_ITEM
import io.github.mcx360.hyprtracker.ui.HISTRORY_SCREEN_TAG
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

    @Test
    fun assertThatNormalBloodPressureDisplaysProperly(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("110")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("73")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("70")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(HISTRORY_SCREEN_TAG).performClick()

        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("110")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("73")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("70")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("Normal")))
    }

    @Test
    fun assertThatElevatedBloodPressureIsDisplayed(){

        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("125")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("76")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("74")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(HISTRORY_SCREEN_TAG).performClick()

        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("125")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("76")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("74")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("Elevated")))
    }

    @Test
    fun assertThatStageOneBloodPressureIsDisplayed(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("136")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("86")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("84")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(HISTRORY_SCREEN_TAG).performClick()

        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("136")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("86")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("84")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("Hypertension Stage 1")))
    }

    @Test
    fun assertThatStageTwoBloodPressureIsDisplayed(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("150")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("100")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("103")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(HISTRORY_SCREEN_TAG).performClick()

        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("150")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("100")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("103")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("Hypertension Stage 2")))
    }

    @Test
    fun assertThatHypertensionCrisisIsDisplayed(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("200")
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("150")
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("120")

        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(HISTRORY_SCREEN_TAG).performClick()

        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("200")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("150")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("120")))
        composeTestRule.onAllNodesWithTag(HISTORY_TAB_ITEM).filter(hasAnyChild(hasText("Hypertension Crisis")))
    }

    @Test
    fun assertThatConfirmButtonOnlyClearsWhenSystolicAndDiastolicValuesAreInputted(){
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("200")
        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText("200"))

        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).performTextInput("120")
        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).assert(hasText("120"))

        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).performTextInput("150")
        composeTestRule.onNodeWithTag(CONFIRM_BUTTON_TAG).performClick()

        composeTestRule.onNodeWithTag(SYSTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
        composeTestRule.onNodeWithTag(DIASTOLIC_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
        composeTestRule.onNodeWithTag(PULSE_OUTLINEDTEXTFIELD_TAG).assert(hasText(""))
    }
}