package com.example.beachapp;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.Intents.*;
import static org.hamcrest.Matchers.*;

// Black box testing
@RunWith(AndroidJUnit4.class)
public class WeatherAPITest {

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
    public void testSantaMonicaCurrentWeatherInfo() {
        String email = "ashtonch@usc.edu";
        String password = "1234567";
        String beachID = "beach001";
        String userID = "-OBDdrw3jhrd0yTm7VkL";
        onView(withId(R.id.editTextEmail))
                .perform(typeText(email), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent.putExtra("beachID", beachID);
        intent.putExtra("userID", userID);

        ActivityScenario.launch(intent);
        SystemClock.sleep(2000);

        intended(hasComponent(DisplayBeachActivity.class.getName()));
        onView(withId(R.id.weatherTemperature))
                .check(matches(not(withText(""))));
        onView(withId(R.id.weatherWindSpeed))
                .check(matches(not(withText(""))));
    }

    @Test
    public void testSantaMonicaCurrentWaveInfo() {
        String email = "ashtonch@usc.edu";
        String password = "1234567";
        String beachID = "beach001";
        String userID = "-OBDdrw3jhrd0yTm7VkL";
        onView(withId(R.id.editTextEmail))
                .perform(typeText(email), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent.putExtra("beachID", beachID);
        intent.putExtra("userID", userID);

        ActivityScenario.launch(intent);
        SystemClock.sleep(2000);

        intended(hasComponent(DisplayBeachActivity.class.getName()));
        onView(withId(R.id.waveHeight))
                .check(matches(not(withText(""))));
    }

    @Test
    public void testSantaMonicaForecastInfo() {
        String email = "ashtonch@usc.edu";
        String password = "1234567";
        String beachID = "beach001";
        String userID = "-OBDdrw3jhrd0yTm7VkL";
        onView(withId(R.id.editTextEmail))
                .perform(typeText(email), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent.putExtra("beachID", beachID);
        intent.putExtra("userID", userID);

        ActivityScenario.launch(intent);
        SystemClock.sleep(2000);

        intended(hasComponent(DisplayBeachActivity.class.getName()));
        for (int i = 1; i <= 8; i++) {
            int cellId = ApplicationProvider.
                    getApplicationContext().
                    getResources().
                    getIdentifier("cell" + i, "id",
                            ApplicationProvider.getApplicationContext().getPackageName());
            onView(withId(cellId))
                    .check(matches(not(withText(""))));
        }
    }

    @Test
    public void testTwoBeachesGetInfo() {
        String email = "ashtonch@usc.edu";
        String password = "1234567";
        String santaMonicaID = "beach001";
        String manhattanID = "beach002";
        String userID = "-OBDdrw3jhrd0yTm7VkL";
        onView(withId(R.id.editTextEmail))
                .perform(typeText(email), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        Intent intent1 = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent1.putExtra("beachID", santaMonicaID);
        intent1.putExtra("userID", userID);

        ActivityScenario.launch(intent1);
        SystemClock.sleep(2000);
        
        onView(withId(R.id.weatherTemperature))
                .check(matches(not(withText(""))));
        onView(withId(R.id.weatherWindSpeed))
                .check(matches(not(withText(""))));
        onView(withId(R.id.waveHeight))
                .check(matches(not(withText(""))));

        Intent intent2 = new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class);
        ActivityScenario.launch(intent2);
        SystemClock.sleep(1000);

        Intent intent3 = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent3.putExtra("beachID", manhattanID);
        intent3.putExtra("userID", userID);

        ActivityScenario.launch(intent3);
        SystemClock.sleep(2000);

        onView(withId(R.id.weatherTemperature))
                .check(matches(not(withText(""))));
        onView(withId(R.id.weatherWindSpeed))
                .check(matches(not(withText(""))));
        onView(withId(R.id.waveHeight))
                .check(matches(not(withText(""))));
    }

    @Test
    public void testSubmitReviewAndReturnToBeachInfo() {
        String email = "ashtonch@usc.edu";
        String password = "1234567";
        String beachID = "beach001";
        String userID = "-OBDdrw3jhrd0yTm7VkL";

        onView(withId(R.id.editTextEmail))
                .perform(typeText(email), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(password), androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent.putExtra("beachID", beachID);
        intent.putExtra("userID", userID);
        ActivityScenario.launch(intent);

        SystemClock.sleep(2000);

        intended(hasComponent(DisplayBeachActivity.class.getName()));
        onView(withId(R.id.weatherTemperature))
                .check(matches(not(withText(""))));
        onView(withId(R.id.weatherWindSpeed))
                .check(matches(not(withText(""))));
        onView(withId(R.id.waveHeight))
                .check(matches(not(withText(""))));

        onView(withId(R.id.buttonRating)).perform(click());
        onView(withId(R.id.buttonAddReview)).perform(click());

        intended(hasComponent(AddReviewActivity.class.getName()));
        onView(withId(R.id.ratingBar)).perform(click());
        onView(withId(R.id.editTextReviewText))
                .perform(typeText("Great beach!"),
                        androidx.test.espresso.action.ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonSubmitReview)).perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.weatherTemperature))
                .check(matches(not(withText(""))));
        onView(withId(R.id.weatherWindSpeed))
                .check(matches(not(withText(""))));
        onView(withId(R.id.waveHeight))
                .check(matches(not(withText(""))));
    }
}
