package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
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
        beachNamesIDPair = new HashMap<>();
        fetchUserData();
        fetchUserReviews();
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
}
