package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ExecutorService executorService;
    private String weatherJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // addBeachDataToFirebase();
        //addReviewDataToFirebase("beach001");
        //updateBeachRating("beach001",5,4);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addBeachDataToFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // Create a hardcoded Beach object
        Beach beach = new Beach(
                "beach001",               // beachID
                "Santa Monica Beach",     // name
                "8 AM to 9 PM",           // accessHours
                34.0195,                  // latitude
                -118.4912                 // longitude
        );
        beach.setPhotoUrl("https://example.com/santamonica.jpg");  // Sample photo URL
        beach.setAvgRating(0.0);  // Initial average rating
        beach.setTotalRatings(0); // Initial total ratings

        // Adding a few activity tags for testing purposes
        beach.addActivityTagID("surfing");
        beach.addActivityTagID("swimming");

        // Push the Beach object to Firebase
        databaseReference.child("beaches").child(beach.getBeachID()).setValue(beach)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Beach added for testing", Toast.LENGTH_SHORT).show();

                        // Now add two Review objects
                        addReviewDataToFirebase(beach.getBeachID());
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add beach: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addReviewDataToFirebase(String beachID) {
        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews").child(beachID);

        // Create two hardcoded Review objects
        Review review1 = new Review(
                "review001",
                "user001",   // Replace with actual user IDs if available
                beachID,
                5,
                "Amazing beach with beautiful sunsets!",
                null // No pictures for this review
        );

        Review review2 = new Review(
                "review002",
                "user002",   // Replace with actual user IDs if available
                beachID,
                4,
                "Great place to relax and enjoy the ocean.",
                null // No pictures for this review
        );

        // Push the first review to Firebase
        reviewsRef.child(review1.getReviewID()).setValue(review1)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Review 1 added for testing", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add review 1: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Push the second review to Firebase
        reviewsRef.child(review2.getReviewID()).setValue(review2)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Review 2 added for testing", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add review 2: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Update the beach's average rating and total ratings
        updateBeachRating(beachID, review1.getRating(), review2.getRating());
    }

    private void updateBeachRating(String beachID, int rating1, int rating2) {
        DatabaseReference beachRef = FirebaseDatabase.getInstance().getReference("beaches").child(beachID);

        // Calculate new average rating
        int totalRatings = 2;
        double avgRating = (double) (rating1 + rating2) / totalRatings;

        // Update the beach object
        beachRef.child("avgRating").setValue(avgRating);
        beachRef.child("totalRatings").setValue(totalRatings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Beach rating updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update beach rating: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
