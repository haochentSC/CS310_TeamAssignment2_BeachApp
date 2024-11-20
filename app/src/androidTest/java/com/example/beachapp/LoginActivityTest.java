package com.example.beachapp;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSuccessfulLoginStartsMapsActivity() {

        onView(withId(R.id.editTextEmail))
                .perform(typeText("ashtonch@usc.edu"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("1234567"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        intended(AllOf.allOf(
                IntentMatchers.hasComponent(MapsActivity.class.getName()),
                IntentMatchers.hasExtra(equalTo("userID"), notNullValue())
        ));
    }

    @Test
    public void testFailedLoginDoesNotStartMapsActivity() {
        onView(withId(R.id.editTextEmail))
                .perform(typeText("ashtonch@usc.edu"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("wrongPassword"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        intended(IntentMatchers.hasComponent(MapsActivity.class.getName()), times(0));
    }
}

