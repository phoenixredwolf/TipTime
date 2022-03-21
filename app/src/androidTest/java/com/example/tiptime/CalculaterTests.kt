package com.example.tiptime

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculaterTests {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calculate_default_tip() {
        onView(withId(R.id.et_cost_of_service))
            .perform(typeText("50.00"))

        onView(withId(R.id.btn_calculate)).perform(scrollTo(),click())

        onView(withId(R.id.tv_tip_result_amount))
            .check(matches(withText(containsString("10.00"))))
        onView(withId(R.id.tv_total_amount))
            .check(matches((withText(containsString("60.00")))))
        onView(withId(R.id.tv_total_amount_per))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_tip_result_per_person_amt))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun calculate_split_tip_without_round_multiple_people() {
        onView(withId(R.id.et_cost_of_service)).perform(typeText("23.76"))

        onView(withId(R.id.btn_decrease_pct)).perform(click(), click(), click())

        onView(withId(R.id.btn_increase_per)).perform(click(), click(), click())

        onView(withId(R.id.sw_round_up)).perform(click())

        onView(withId(R.id.btn_calculate)).perform(click())

        onView(withId(R.id.tv_tip_result_amount))
            .check(matches((withText(containsString("4.04")))))
        onView(withId(R.id.tv_total_amount))
            .check(matches((withText(containsString("27.80")))))
        onView(withId(R.id.tv_total_amount_per))
            .check(matches((withText(containsString("6.95")))))
        onView(withId(R.id.tv_tip_result_per_person_amt))
            .check(matches(withText(containsString("1.01"))))
    }
}