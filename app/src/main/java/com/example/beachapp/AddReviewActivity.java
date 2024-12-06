package com.example.beachapp;

import androidx.appcompat.app.AppCompatActivity;
import com.example.beachapp.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Collections;

public class AddReviewActivity extends AppCompatActivity {
    private EditText editTextReviewText;
    private RatingBar ratingBar;
    private Button buttonSubmitReview;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reviewsRef;
    private DatabaseReference beachRef;
    private String beachID=null;
    private String userID=null;
    private Button buttonAddPhoto;
    private ImageView imageViewSelectedPhoto;

    private final String[] photoChoices = {
            "alamitosbeach",
            "huntingtonbeach",
            "manhattanbeach",
            "santamonica",
            "newportbeach"
    };
    private String chosenPhotoName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        editTextReviewText = findViewById(R.id.editTextReviewText);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSubmitReview = findViewById(R.id.buttonSubmitReview);

        buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        imageViewSelectedPhoto = findViewById(R.id.imageViewSelectedPhoto);

        beachID = getIntent().getStringExtra("beachID");
        userID = getIntent().getStringExtra("userID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reviewsRef = firebaseDatabase.getReference("reviews").child(beachID);
        beachRef = firebaseDatabase.getReference("beaches").child(beachID);
        if (beachID == null || userID == null) {
            Toast.makeText(this, "Error: Missing beachID or userID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        buttonSubmitReview.setOnClickListener(v -> {
            pushReview();
        });

        buttonAddPhoto.setOnClickListener(v -> {
            showPhotoSelectionDialog();
        });
    }

    private void showPhotoSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Photo");

        builder.setItems(new CharSequence[]{
                "AlamitosBeach", "HuntingtonBeach", "ManhattanBeach", "SantaMonica", "NewportBeach"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosenPhotoName = photoChoices[which];

                int resId = getResources().getIdentifier(chosenPhotoName, "drawable", getPackageName());
                imageViewSelectedPhoto.setImageResource(resId);
                imageViewSelectedPhoto.setVisibility(ImageView.VISIBLE);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void pushReview() {
        String reviewText = editTextReviewText.getText().toString();
        int rating = (int) ratingBar.getRating();
        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "Please enter your review.", Toast.LENGTH_SHORT).show();
            return;
        }
        String reviewID = reviewsRef.push().getKey();
        Review review;
        if (chosenPhotoName != null) {
            review = new Review(reviewID, userID, beachID, rating, reviewText, Collections.singletonList(chosenPhotoName));
        } else {
            review = new Review(reviewID, userID, beachID, rating, reviewText, null);
        }
        reviewsRef.child(reviewID).setValue(review)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddReviewActivity.this, "Review submitted successfully.", Toast.LENGTH_SHORT).show();
                        updateBeachRating(rating);
                    } else {
                        Toast.makeText(AddReviewActivity.this, "Failed to submit review: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void updateBeachRating(int newRating){
        beachRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                Beach beach = snapshot.getValue(Beach.class);
                    int totalRatings = beach.getTotalRatings();
                    double avgRating = beach.getAvgRating();
                    double newAvgRating = ((avgRating * totalRatings) + newRating) / (totalRatings + 1);
                    beach.setAvgRating(newAvgRating);
                    beach.setTotalRatings(totalRatings + 1);
                    beachRef.setValue(beach).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Intent intent = new Intent(AddReviewActivity.this, DisplayBeachActivity.class);
                                    intent.putExtra("beachID", beachID);
                                    intent.putExtra("userID", userID);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AddReviewActivity.this, "Failed to update beach rating: " + updateTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
            } else {
                Toast.makeText(AddReviewActivity.this, "Failed to get beach data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
