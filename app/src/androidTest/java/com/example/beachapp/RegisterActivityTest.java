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
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {
    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSuccessfulRegisterStartsLoginActivity() {
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String randomUsername = "TestUser" + randomSuffix;
        String randomEmail = "testuser" + randomSuffix + "@example.com";
        String password = "password123"; // Password can remain the same
        onView(withId(R.id.editTextUsername))
                .perform(typeText(randomUsername));
        onView(withId(R.id.editTextEmail))
                .perform(typeText(randomEmail));
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password));
        onView(withId(R.id.buttonRegister))
                .perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

}
