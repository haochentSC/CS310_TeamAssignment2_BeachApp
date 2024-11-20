package com.example.beachapp;

import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RunWith(AndroidJUnit4.class)
public class ReviewActivityTest {

    @Before
    public void setUp() {
        Intents.init();
    }
    @After
    public void tearDown() {
        Intents.release();
    }
    @Test
    public void testDeleteReview() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), PortfolioActivity.class);
        intent.putExtra("userID", "-OBDdrw3jhrd0yTm7VkL");
        intent.putExtra("beachID", "beach001");
        ActivityScenario<PortfolioActivity> scenario = ActivityScenario.launch(intent);
        final int[] initialSize = new int[1];
        scenario.onActivity(activity-> {
            initialSize[0] = activity.userReviewList.size();
        });
        onView(withId(R.id.editTextReviewNumber))
                .perform(typeText("1"));
        onView(withId(R.id.buttonDeleteReview))
                .perform(click());
        final int[] deletedSize = new int[1];
        scenario.onActivity(activity -> {
            deletedSize[0] = activity.userReviewList.size();
        });
        assertEquals(initialSize[0] , deletedSize[0]);
    }
    @Test
    public void testAddReview() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ReviewActivity.class);
        intent.putExtra("beachID", "beach001");
        intent.putExtra("userID", "-OBDdrw3jhrd0yTm7VkL");
        ActivityScenario.launch(intent);
        onView(withId(R.id.buttonAddReview)).perform(click());

        intended(hasComponent(AddReviewActivity.class.getName()));
        onView(withId(R.id.editTextReviewText)).perform(typeText("Great beach!"));
        onView(withId(R.id.ratingBar)).perform(setRating(5));
        onView(withId(R.id.buttonSubmitReview)).perform(click());
        intended(hasComponent(DisplayBeachActivity.class.getName()));
        intended(allOf(
                hasComponent(DisplayBeachActivity.class.getName()),
                hasExtra("beachID", "beach001"),
                hasExtra("userID", "-OBDdrw3jhrd0yTm7VkL")
        ));
    }


    public static ViewAction setRating(final float rating) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(RatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((RatingBar) view).setRating(rating);
            }
        };
    }
}

