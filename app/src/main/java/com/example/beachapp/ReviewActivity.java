package com.example.beachapp;
import com.example.beachapp.Review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private String beachID;
    private String userID;
    private List<Review> reviewList;

    private TextView textViewReviews;
    private Button buttonAddReview;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        textViewReviews = findViewById(R.id.textViewReviews);
        buttonAddReview = findViewById(R.id.buttonAddReview);
        reviewList = new ArrayList<>();
        beachID = getIntent().getStringExtra("beachID");
        userID = getIntent().getStringExtra("userID");

        if (beachID == null || userID == null) {
            Toast.makeText(this, "Error: Missing beachID or userID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_review = firebaseDatabase.getReference("reviews").child(beachID);

        fetchReviews();

        buttonAddReview.setOnClickListener(v ->
        {
            Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
            intent.putExtra("beachID", beachID);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
    }
    public void fetchReviews(){
        databaseReference_review.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for(DataSnapshot reviewSnapShot : snapshot.getChildren()){
                    Review curReview= reviewSnapShot.getValue(Review.class);
                    if(curReview!=null){
                        reviewList.add(curReview);
                    }
                }
                displayReviews();
                updateAverageRating();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewActivity.this, "Failed to load reviews: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void displayReviews(){
        String allReviews="";
        for(int i=0; i<reviewList.size(); i++){
            Review review = reviewList.get(i);
            allReviews += "Review " + (i + 1) + "\n";
            allReviews += "Rating: " + review.getRating() + " ★\n";
            allReviews += "Comment: " + review.getText() + "\n";
            if (review.getPictureUrls() != null && !review.getPictureUrls().isEmpty()) {
                allReviews += "Pictures: " + review.getPictureUrls().size() + " attached.\n";
            }
            allReviews += "------------------------------\n";
        }
        textViewReviews.setText(allReviews);
        Toast.makeText(ReviewActivity.this, "reviews Loaded,String size:"+allReviews.length(), Toast.LENGTH_LONG).show();
    }
    private void updateAverageRating() {
        double totalRating = 0;
        int count = reviewList.size();
        for (Review review : reviewList) {
            totalRating += review.getRating();
        }
        double averageRating=0;
        if(count>0) {
            averageRating = totalRating /count;
        }
        DatabaseReference beachRef = FirebaseDatabase.getInstance().getReference("beaches").child(beachID);
        beachRef.child("avgRating").setValue(averageRating);
        beachRef.child("totalRatings").setValue(count);

    }
}
