package com.example.sowitplot

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.Matchers.not
import androidx.test.espresso.Root
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Simple sanity test:
     * spinner is visible and shows the placeholder text "Select plot".
     */
    @Test
    fun spinnerShowsPlaceholderText() {
        onView(withId(R.id.spinnerPlots))
            .check(matches(isDisplayed()))
            .check(matches(withSpinnerText("Select plot")))
    }

    /**
     * When the user taps the list FAB, the bottom sheet should open
     * and show both the title "My plots" and the RecyclerView.
     */
    @Test
    fun openBottomSheet_showsRecyclerList() {
        // Click FAB that opens the list
        onView(withId(R.id.btnOpenPlotsBottomSheet))
            .perform(click())

        // Title text from bottom_sheet_plots.xml
        onView(withText("My plots"))
            .check(matches(isDisplayed()))

        // RecyclerView for the list of plots
        onView(withId(R.id.recyclerPlots))
            .check(matches(isDisplayed()))
    }

    /**
     * If the user taps "Save" with less than 3 points, a Toast error should appear.
     * We don't interact with the map at all here (0 points by default).
     */
    @Test
    fun savePlotWithNoPoints_showsToastError() {
        // Click Save without drawing anything
        onView(withId(R.id.btnSavePlot))
            .perform(click())

        // Check Toast text
        onView(withText("Add at least 3 points to form a polygon"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    /**
     * More advanced flow:
     *  - open bottom sheet
     *  - press Back
     *  - verify that the bottom sheet content is gone.
     */
    @Test
    fun bottomSheetOpensAndClosesWithBack() {
        // Open bottom sheet
        onView(withId(R.id.btnOpenPlotsBottomSheet))
            .perform(click())

        // Make sure it is open
        onView(withId(R.id.recyclerPlots))
            .check(matches(isDisplayed()))

        // Simulate back press to close BottomSheetDialog
        pressBack()

        // RecyclerView should disappear from the view hierarchy
        onView(withId(R.id.recyclerPlots))
            .check(doesNotExist())
    }
}

/**
 * Custom matcher to detect Toast windows.
 */
class ToastMatcher : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        // TYPE_TOAST = 2005 on modern Android, but we use this generic check
        val type = root.windowLayoutParams?.get()?.type ?: return false
        if (type == android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ||
            type == android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL ||
            type == android.view.WindowManager.LayoutParams.TYPE_TOAST
        ) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            // Toasts are not contained in any app window
            return windowToken === appToken
        }
        return false
    }
}
