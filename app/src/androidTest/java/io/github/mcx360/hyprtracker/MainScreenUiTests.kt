package io.github.mcx360.hyprtracker

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.github.mcx360.hyprtracker.ui.mainScreen.ABOUT_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.BACKUP_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.BIN_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.HyprTrackerScreen
import io.github.mcx360.hyprtracker.ui.mainScreen.MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.RATE_APP_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.REPORT_BUG_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG
import io.github.mcx360.hyprtracker.ui.mainScreen.USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG
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
        composeTestRule.onNodeWithTag(EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(BIN_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(BACKUP_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(RATE_APP_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(REPORT_BUG_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
        composeTestRule.onNodeWithTag(ABOUT_IN_NAVIGATIONDRAWER_TAG).assertHasClickAction()
    }
}