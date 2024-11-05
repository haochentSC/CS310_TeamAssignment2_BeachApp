package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayBeachActivity extends AppCompatActivity {
    private ImageView imageViewBeachPhoto;
    private TextView textViewBeachName;
    private TextView textViewAccessHours;
    private Button buttonRating;

    private String beachID;
    private String userID;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference_beach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_beach);
        imageViewBeachPhoto = findViewById(R.id.imageViewBeachPhoto);
        textViewBeachName = findViewById(R.id.textViewBeachName);
        textViewAccessHours = findViewById(R.id.textViewAccessHours);
        buttonRating = findViewById(R.id.buttonRating);
        Intent intent=getIntent();
        if (intent != null) {
            beachID = intent.getStringExtra("beachID");
            userID = intent.getStringExtra("userID");
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_beach = firebaseDatabase.getReference("beaches");

        fetchBeachData();

        buttonRating.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(DisplayBeachActivity.this, ReviewListActivity.class);
            reviewIntent.putExtra("beachID", beachID);
            reviewIntent.putExtra("userID", userID);
            startActivity(reviewIntent);
        });
    }
    private void fetchBeachData() {
        databaseReference_beach=databaseReference_beach.child(beachID);
        databaseReference_beach.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Beach beach = snapshot.getValue(Beach.class);
                if (beach != null) {
                    Toast.makeText(DisplayBeachActivity.this, "Beach found"+ beach.getBeachID(), Toast.LENGTH_LONG).show();
                    textViewBeachName.setText(beach.getName());
                    textViewAccessHours.setText("Access Hours: " + beach.getAccessHours());
                    /*
                    Glide.with(DisplayBeachActivity.this)
                            .load(R.drawable.placeholder_image)
                            .into(imageViewBeachPhoto);
                     */
                    buttonRating.setText(String.format("Rating: %.1f ★", beach.getAvgRating()));
                }
                else{
                    textViewBeachName.setText("Beach not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textViewBeachName.setText("Error loading beach data");
            }
        });
    }
}
