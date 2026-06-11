package com.example.novelrepo

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.novelrepo.BookHorizontalAdapter.VH
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun bottomNav_opensLibraryActivity() {
        ActivityScenario.launch(MainActivity::class.java)

        // Click the Library tab
        onView(withId(R.id.nav_library)).perform(click())

        // Wait briefly for the Activity to launch
        Thread.sleep(1000)

        // Check that LibraryActivity is displayed
        onView(withId(R.id.libraryRecycler))
            .check(matches(isDisplayed()))
    }
}

