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

import java.util.ArrayList;
import java.util.List;
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void addBeachDataToFirebase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // List to hold all Beach objects
        List<Beach> beaches = new ArrayList<>();

        // 1. Santa Monica Beach
        Beach santamonica = new Beach(
                "beach001",               // beachID
                "Santa Monica Beach",     // name
                "8 AM to 9 PM",           // accessHours
                34.01943,                 // latitude
                -118.48968                // longitude
        );
        santamonica.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Santa_Monica_Pier_July_2019.jpg/800px-Santa_Monica_Pier_July_2019.jpg"); // Replace with actual URL
        santamonica.setAvgRating(0.0);  // Initial average rating
        santamonica.setTotalRatings(0); // Initial total ratings
        santamonica.addActivityTagID("surfing");
        santamonica.addActivityTagID("swimming");
        beaches.add(santamonica);

        // 2. Manhattan Beach
        Beach manhattan = new Beach(
                "beach002",               // beachID
                "Manhattan Beach",        // name
                "8 AM to 9 PM",           // accessHours
                33.88477,                 // latitude
                -118.41103                // longitude
        );
        manhattan.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Manhattan_Beach_Pier_%28cropped%29.jpg/800px-Manhattan_Beach_Pier_%28cropped%29.jpg"); // Replace with actual URL
        manhattan.setAvgRating(0.0);
        manhattan.setTotalRatings(0);
        manhattan.addActivityTagID("surfing");
        manhattan.addActivityTagID("sunbathing");
        beaches.add(manhattan);

        // 3. Alamitos Beach
        Beach alamitos = new Beach(
                "beach003",               // beachID
                "Alamitos Beach",         // name
                "8 AM to 9 PM",           // accessHours
                33.763,                   // latitude
                -118.1749                 // longitude
        );
        alamitos.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Alamitos_Beach.JPG/800px-Alamitos_Beach.JPG"); // Replace with actual URL
        alamitos.setAvgRating(0.0);
        alamitos.setTotalRatings(0);
        alamitos.addActivityTagID("swimming");
        alamitos.addActivityTagID("sunbathing");
        beaches.add(alamitos);

        // 4. Huntington Beach
        Beach huntington = new Beach(
                "beach004",               // beachID
                "Huntington Beach",       // name
                "8 AM to 10 PM",          // accessHours
                33.65953,                 // latitude
                -117.99984                // longitude
        );
        huntington.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Huntington_Beach.jpg/800px-Huntington_Beach.jpg"); // Replace with actual URL
        huntington.setAvgRating(0.0);
        huntington.setTotalRatings(0);
        huntington.addActivityTagID("surfing");
        huntington.addActivityTagID("sunbathing");
        beaches.add(huntington);

        // 5. Newport Beach
        Beach newport = new Beach(
                "beach005",               // beachID
                "Newport Beach",          // name
                "8 AM to 9 PM",           // accessHours
                33.60493,                 // latitude
                -117.87487                // longitude
        );
        newport.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Newport_Beach.JPG/800px-Newport_Beach.JPG"); // Replace with actual URL
        newport.setAvgRating(0.0);
        newport.setTotalRatings(0);
        newport.addActivityTagID("yachting");
        newport.addActivityTagID("sunbathing");
        beaches.add(newport);

        // Iterate through the list and add each beach to Firebase
        for (Beach beach : beaches) {
            databaseReference.child("beaches").child(beach.getBeachID()).setValue(beach)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), beach.getName() + " added successfully", Toast.LENGTH_SHORT).show();

                            // Optionally, add reviews or other related data here
                            // addReviewDataToFirebase(beach.getBeachID());
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to add " + beach.getName() + ": " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}
