package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewReviews;
    private LinearLayout linearLayoutReviews;
    private DatabaseReference usersRef;
    private DatabaseReference reviewsRef;
    private List<Review> userReviewList;
    private String userID;
    private String beachID;
    private DatabaseReference beachesRef;
    private Map<String, String> beachNamesIDPair;
    private int pc;
    private EditText editTextReviewNumber;
    private Button buttonDeleteReview;
    private Button buttonGoBackToBeach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        Intent intent=getIntent();
        beachID = intent.getStringExtra("beachID");
        userID = intent.getStringExtra("userID");
        if (userID == null) {
            Toast.makeText(this, "UserID is null, ERROR. in PortfolioActivity", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        usersRef=FirebaseDatabase.getInstance().getReference("users");
        reviewsRef=FirebaseDatabase.getInstance().getReference("reviews");
        beachesRef=FirebaseDatabase.getInstance().getReference("beaches");
        textViewUsername= findViewById(R.id.textViewUsername);
        textViewEmail= findViewById(R.id.textViewEmail);
        userReviewList= new ArrayList<>();
        textViewReviews= findViewById(R.id.textViewReviews);
        editTextReviewNumber = findViewById(R.id.editTextReviewNumber);
        buttonDeleteReview = findViewById(R.id.buttonDeleteReview);
        beachNamesIDPair = new HashMap<>();
        buttonGoBackToBeach = findViewById(R.id.buttonGoBackToBeach);
        fetchUserData();
        fetchUserReviews();
        buttonDeleteReview.setOnClickListener(v -> {
            String input= editTextReviewNumber.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Need a review number.", Toast.LENGTH_SHORT).show();
                return;
            }
            int reviewNumber;
            try {
                reviewNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid review number.", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteReviewByNumber(reviewNumber);

        });
        buttonGoBackToBeach.setOnClickListener(v -> {
            Intent backIntent = new Intent(PortfolioActivity.this, DisplayBeachActivity.class);
            backIntent.putExtra("beachID", beachID);
            backIntent.putExtra("userID", userID);
            startActivity(backIntent);
        });
    }
    private void deleteReviewByNumber(int reviewNumber) {
        if (reviewNumber < 1 || reviewNumber > userReviewList.size()) {
            Toast.makeText(this, "Review number out of range.", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = userReviewList.get(reviewNumber - 1);
        DatabaseReference delete_reviewRef = FirebaseDatabase.getInstance().getReference("reviews").child(review.getBeachID()).child(review.getReviewID());
        delete_reviewRef.removeValue();
        updateBeachRatingAfterDeletion(review);
        userReviewList.remove(review);
        displayUserReviews();
    }

    private void fetchUserData() {
        usersRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    textViewUsername.setText(user.getUsername());
                    textViewEmail.setText(user.getEmail());
                } else {
                    textViewUsername.setText("Username not found in PortfolioActivity");
                    textViewEmail.setText("Email not found in PortfolioActivity ");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textViewUsername.setText("Error load username in PortfolioActivity");
                textViewEmail.setText("Error load email in PortfolioActivity");
            }
        });
    }

    private void fetchUserReviews(){
        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userReviewList.clear();
                for (DataSnapshot beachSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot reviewSnapshot : beachSnapshot.getChildren()) {
                        Review review = reviewSnapshot.getValue(Review.class);
                        if (review != null && review.getUserID().equals(userID)) {
                            userReviewList.add(review);
                        }
                    }
            }
                if (userReviewList.isEmpty()) {
                    textViewReviews.setText("You haven't submitted any reviews yet.");
                } else {
                    fetchBeachNamesAndDisplayReviews();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PortfolioActivity.this, "ERROR in PortfolioActivity load reviews: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchBeachNamesAndDisplayReviews() {
        pc =userReviewList.size();
        for (Review review : userReviewList) {
            String beachID = review.getBeachID();
            if (beachNamesIDPair.containsKey(beachID)) {
                pc-=1;
                if (pc == 0) {
                    displayUserReviews();
                }
            }
            else{
                beachesRef.child(beachID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String beachName = snapshot.child("name").getValue(String.class);
                        beachNamesIDPair.put(beachID, beachName);
                        pc-=1;
                        if (pc== 0) {
                            displayUserReviews();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        beachNamesIDPair.put(beachID, "beachName unknown/not found");
                        pc-=1;
                        if (pc == 0) {
                            displayUserReviews();
                        }
                    }
                });
            }
        }

    }
    public void displayUserReviews(){
        String allReviews="";
        for(int i=0; i<userReviewList.size(); i++){
            Review review = userReviewList.get(i);
            String beachName = beachNamesIDPair.get(review.getBeachID());
            allReviews += "Review " + (i + 1) + "\n";
            allReviews+="Beach: "+beachName+ "\n";
            allReviews += "Rating: " + review.getRating() + " ★\n";
            allReviews += "Comment: " + review.getText() + "\n";
            allReviews += "------------------------------\n";
        }
        textViewReviews.setText(allReviews);
    }
    public void updateBeachRatingAfterDeletion(Review delReview){
        DatabaseReference del_beachRef = beachesRef.child(delReview.getBeachID());
        del_beachRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Beach b = snapshot.getValue(Beach.class);
                if (b != null) {
                    int totalRatings = b.getTotalRatings();
                    double avgRating = b.getAvgRating();
                    if (totalRatings > 1) {
                        double newAvgRating = ((avgRating * totalRatings) - delReview.getRating()) / (totalRatings - 1);
                        b.setAvgRating(newAvgRating);
                        b.setTotalRatings(totalRatings - 1);
                    } else {
                        b.setAvgRating(0.0);
                        b.setTotalRatings(0);
                    }
                    del_beachRef.setValue(b);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PortfolioActivity.this, "Failed to update beach rating: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
