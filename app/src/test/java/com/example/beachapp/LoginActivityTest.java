package com.example.beachapp;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;

import com.google.firebase.FirebaseApp;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {30})
public class LoginActivityTest {
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(context);
    }
    @Test
    public void testSuccessfulLoginToast() {
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            EditText emailField = activity.findViewById(R.id.editTextEmail);
            EditText passwordField = activity.findViewById(R.id.editTextPassword);
            Button loginButton = activity.findViewById(R.id.buttonLogin);

            emailField.setText("ashtonch@usc.edu");
            passwordField.setText("1234567");
            loginButton.performClick();
            String toastText = ShadowToast.getTextOfLatestToast();
            assertEquals("Login successful", toastText);
        });
    }
    @Test
    public void testFailedLoginToast() {
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {

            EditText emailField = activity.findViewById(R.id.editTextEmail);
            EditText passwordField = activity.findViewById(R.id.editTextPassword);
            Button loginButton = activity.findViewById(R.id.buttonLogin);
            emailField.setText("ashtonch@usc.edu");
            passwordField.setText("wrongPassword");
            loginButton.performClick();
            String toastText = ShadowToast.getTextOfLatestToast();
            assertEquals("Incorrect password", toastText);
        });
    }
}
