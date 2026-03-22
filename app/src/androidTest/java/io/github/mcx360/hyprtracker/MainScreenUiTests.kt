package io.github.mcx360.hyprtracker

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.github.mcx360.hyprtracker.ui.MainScreen.ABOUT_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.BACKUP_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.BIN_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.EXPORTLOGS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.MainScreen.MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.RATEAPP_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.REPORTBUG_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.SHARELOGS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.MainScreen.USERSANDSETTINGS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.theme.HyprTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainScreenUiTests {

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
    fun CheckNavigationDrawerOpensAndShowsCorrectly(){
        composeTestRule.onNodeWithTag(NAVIGATIONDRAWER_TAG).performClick()
        composeTestRule.onNodeWithTag(NAVIGATIONDRAWER_TAG).assertIsDisplayed()
    }

    @Test
    fun checkNavigationDrawerContentsAreAllClickable(){
        composeTestRule.onNodeWithTag(NAVIGATIONDRAWER_TAG).performClick()
        composeTestRule.onNodeWithTag(EXPORTLOGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(SHARELOGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(BIN_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(BACKUP_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(RATEAPP_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(REPORTBUG_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(USERSANDSETTINGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(ABOUT_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
    }
}