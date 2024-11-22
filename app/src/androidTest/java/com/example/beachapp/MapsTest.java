package com.example.beachapp;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import static org.junit.Assert.assertEquals;

import com.google.android.gms.maps.model.LatLng;

@RunWith(AndroidJUnit4.class)
public class MapsTest {

    @Rule
    public ActivityScenarioRule<MapsActivity> activityRule = new ActivityScenarioRule<>(MapsActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testInitialMapState() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        activityRule.getScenario().onActivity(activity -> {
            SystemClock.sleep(1000); // Allow the map to stabilize

            LatLng cameraPosition = activity.mMap.getCameraPosition().target;
            float zoomLevel = activity.mMap.getCameraPosition().zoom;

            // Verify initial camera position
            assertEquals(new LatLng(34.01943, -118.48968), new LatLng((double)Math.round(cameraPosition.latitude * 100000d) / 100000d, (double)Math.round(cameraPosition.longitude * 100000d) / 100000d));
            assertEquals(9.6f, zoomLevel, 0.1f);
        });
    }


    @Test
    public void testInfoWindowClickRedirectsToDisplayBeachActivity() {

        // Simulate marker click by its title (e.g., "Santa Monica Beach")
        onView(withText("Santa Monica Beach")).perform(click());

        // Check if the info window is displayed
        onView(withText("Santa Monica Beach")).check(matches(isDisplayed()));

        // Prepare intent for MapsActivity with a userID
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), DisplayBeachActivity.class);
        intent.putExtra("beachID", "beach001");
        intent.putExtra("userID", "-OBDdrw3jhrd0yTm7VkL");

        // Launch MapsActivity
        ActivityScenario.launch(intent);
        SystemClock.sleep(2000); // Wait for the map to load

        // Verify navigation to DisplayBeachActivity
        intended(hasComponent(DisplayBeachActivity.class.getName()));

        // Verify that the intent has the correct extras
        intended(hasExtra("beachID", "beach001"));
        intended(hasExtra("userID", "-OBDdrw3jhrd0yTm7VkL"));
    }


    @Test
    public void testCustomInfoWindowDisplayedWithCorrectPosition() {

        // Simulate tap on Huntington Beach marker
        onView(withText("Santa Monica Beach")).perform(click());
        onView(withText("Huntington Beach")).perform(click());
        SystemClock.sleep(1000); // Allow the map to stabilize
        activityRule.getScenario().onActivity(activity -> {
            SystemClock.sleep(1000); // Allow the map to stabilize

            LatLng cameraPosition = activity.mMap.getCameraPosition().target;

            // Verify initial camera position
            assertEquals(activity.HB.getPosition(), new LatLng((double)Math.round(cameraPosition.latitude * 100000d) / 100000d, (double)Math.round(cameraPosition.longitude * 100000d) / 100000d));
        });
    }


    @Test
    public void testSpinnerDisplaysAllBeachNames() {
        // Perform a click on the spinner to open the dropdown
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Santa Monica Beach"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Manhattan Beach"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Alamitos Beach"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Huntington Beach"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Newport Beach"))).inRoot(isPlatformPopup()).perform(click());
    }

    @Test
    public void testCustomInfoWindowRendering() {
        // Select "Huntington Beach" in the spinner
        onView(withId(R.id.markerSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Huntington Beach"))).perform(click());

        // Verify the custom info window contains the correct title
        onView(withText("Huntington Beach")).check(matches(isDisplayed()));
    }



}
