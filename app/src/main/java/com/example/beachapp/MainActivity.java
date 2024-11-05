package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ExecutorService executorService;
    private String weatherJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addBeachDataToFirebase();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addBeachDataToFirebase() {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("beaches");
        // Create a hardcoded Beach object
        Beach beach = new Beach(
                "beach001",               // beachID
                "Santa Monica Beach",      // name
                "8 AM to 9 PM",            // accessHours
                34.0195,                   // latitude
                -118.4912                  // longitude
        );
        beach.setPhotoUrl("https://example.com/santamonica.jpg");  // Sample photo URL
        beach.setAvgRating(4.5);  // Sample average rating
        beach.setTotalRatings(10); // Sample total ratings

        // Adding a few review IDs and activity tags for testing purposes
        beach.addReviewID("review001");
        beach.addReviewID("review002");
        beach.addActivityTagID("surfing");
        beach.addActivityTagID("swimming");

        // Push the Beach object to Firebase
        databaseReference.child(beach.getBeachID()).setValue(beach)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Beach added for testing", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add beach: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
