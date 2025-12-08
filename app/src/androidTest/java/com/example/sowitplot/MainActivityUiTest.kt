package com.example.sowitplot

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun spinnerShowsPlaceholderText() {
        onView(withId(R.id.spinnerPlots))
            .check(matches(withSpinnerText("Select plot")))
    }

    @Test
    fun openBottomSheet_showsMyPlotsTitle() {
        // this FAB must exist in your layout with this id
        onView(withId(R.id.btnOpenPlotsBottomSheet))
            .perform(click())

        onView(withText("My plots"))
            .check(matches(isDisplayed()))
    }
}
