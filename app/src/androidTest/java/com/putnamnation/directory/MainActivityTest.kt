package com.putnamnation.directory

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.putnamnation.directory.ui.common.UserViewModel
import com.putnamnation.directory.ui.list.ListScreen
import com.putnamnation.directory.ui.theme.AppTheme
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MainActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun navigationTest() {
        val userViewModel = UserViewModel()

        composeTestRule.setContent {
            AppTheme() {
                MainScreenView(userViewModel = userViewModel, WindowSizeClass.COMPACT)
            }
        }
        composeTestRule.mainClock.autoAdvance = true // default

        composeTestRule.onNode(hasTestTag("ListScreen")).assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("MapScreen")).assertDoesNotExist()

        composeTestRule.onNodeWithText("Map").performClick()
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        composeTestRule.onNode(hasTestTag("MapScreen")).assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("ListScreen")).assertDoesNotExist()


        composeTestRule.onNodeWithText("Directory").performClick()
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        composeTestRule.onNode(hasTestTag("ListScreen")).assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("MapScreen")).assertDoesNotExist()
    }


    @Test
    fun listScreenTest() {
        val userViewModel = UserViewModel()
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        composeTestRule.setContent {
            AppTheme() {
                ListScreen(userViewModel, navController)
            }
        }
        composeTestRule.mainClock.autoAdvance = true // default
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        Thread.sleep(500)

        val expandButton =
            composeTestRule.onNode(hasTestTag("ExpandIcon").and(hasParent(hasTestTag("UserView - 1"))))

        expandButton.assertIsDisplayed()
        expandButton.performClick()
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        val expandableNode =
            composeTestRule.onNode(hasTestTag("ExpandableContent").and(hasParent(hasTestTag("UserView - 1"))), useUnmergedTree = true)
        expandableNode.assertIsDisplayed()
    }

    @Test
    fun mapClicktest() {
        val userViewModel = UserViewModel()
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        composeTestRule.setContent {
            AppTheme() {
                MainScreenView(userViewModel = userViewModel, WindowSizeClass.COMPACT)
            }
        }
        composeTestRule.mainClock.autoAdvance = true // default
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        Thread.sleep(500)

        val expandButton =
            composeTestRule.onNode(hasTestTag("ExpandIcon").and(hasParent(hasTestTag("UserView - 1"))))

        expandButton.assertIsDisplayed()
        expandButton.performClick()
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        composeTestRule.onNode(hasTestTag("AddressView").and(hasParent(hasTestTag("UserView - 1")))).performClick()
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        composeTestRule.onNode(hasTestTag("MapScreen")).assertIsDisplayed()


    }
}