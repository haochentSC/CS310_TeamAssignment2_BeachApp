package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // [NEW] For setting visibility
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; // [NEW] Not needed anymore
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewReviewsTitle;
    private TextView textViewNoReviews; // [NEW] For no reviews message
    private LinearLayout reviewsContainer; // [NEW] Container for reviews
    private DatabaseReference usersRef;
    private DatabaseReference reviewsRef;
    public List<Review> userReviewList;
    private String userID;
    private String beachID;
    private DatabaseReference beachesRef;
    private Map<String, String> beachNamesIDPair;
    private int pc;
    private EditText editTextReviewNumber;
    private EditText editTextUpdateReviewNumber; // [NEW] For update review
    private Button buttonDeleteReview;
    private Button buttonUpdateReview; // [NEW] Update Review Button
    private Button buttonGoBackToBeach;
    private Button buttonLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        Intent intent = getIntent();
        beachID = intent.getStringExtra("beachID");
        userID = intent.getStringExtra("userID");

        if (userID == null) {
            Toast.makeText(this, "UserID is null, ERROR. in PortfolioActivity", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        beachesRef = FirebaseDatabase.getInstance().getReference("beaches");

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewReviewsTitle = findViewById(R.id.textViewReviewsTitle);
        textViewNoReviews = findViewById(R.id.textViewNoReviews);
        reviewsContainer = findViewById(R.id.reviewsContainer);
        userReviewList = new ArrayList<>();

        editTextReviewNumber = findViewById(R.id.editTextReviewNumber);
        editTextUpdateReviewNumber = findViewById(R.id.editTextUpdateReviewNumber);

        buttonDeleteReview = findViewById(R.id.buttonDeleteReview);
        buttonUpdateReview = findViewById(R.id.buttonUpdateReview);
        buttonGoBackToBeach = findViewById(R.id.buttonGoBackToBeach);
        buttonLogout =findViewById(R.id.buttonLogout);

        beachNamesIDPair = new HashMap<>();

        fetchUserData();
        fetchUserReviews();

        // Handle Delete Review Button Click
        buttonDeleteReview.setOnClickListener(v -> {
            String input = editTextReviewNumber.getText().toString().trim();
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

        // Handle Update Review Button Click [NEW]
        buttonUpdateReview.setOnClickListener(v -> {
            String input = editTextUpdateReviewNumber.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Need a review number to update.", Toast.LENGTH_SHORT).show();
                return;
            }
            int reviewNumber;
            try {
                reviewNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid review number.", Toast.LENGTH_SHORT).show();
                return;
            }
            updateReviewByNumber(reviewNumber);
        });

        // Handle Go Back Button Click
        buttonGoBackToBeach.setOnClickListener(v -> {
            Intent backIntent = new Intent(PortfolioActivity.this, MapsActivity.class);
            backIntent.putExtra("userID", userID);
            startActivity(backIntent);
        });
        buttonLogout.setOnClickListener(v -> {
            Intent logOutIntent = new Intent(PortfolioActivity.this, LoginActivity.class);
            startActivity(logOutIntent);
        });
    }

    private void deleteReviewByNumber(int reviewNumber) {
        if (reviewNumber < 1 || reviewNumber > userReviewList.size()) {
            Toast.makeText(this, "Review number out of range.", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = userReviewList.get(reviewNumber - 1);
        DatabaseReference delete_reviewRef = FirebaseDatabase.getInstance().getReference("reviews")
                .child(review.getBeachID()).child(review.getReviewID());

        delete_reviewRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateBeachRatingAfterDeletion(review);
                userReviewList.remove(reviewNumber - 1);
                displayUserReviews();
                Toast.makeText(this, "Review deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete review: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateReviewByNumber(int reviewNumber) {
        if (reviewNumber < 1 || reviewNumber > userReviewList.size()) {
            Toast.makeText(this, "Review number out of range.", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = userReviewList.get(reviewNumber - 1);
        int oldRating = review.getRating(); // [NEW] Store old rating

        DatabaseReference delete_reviewRef = FirebaseDatabase.getInstance().getReference("reviews")
                .child(review.getBeachID()).child(review.getReviewID());

        delete_reviewRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update beach rating by removing old rating
                updateBeachRatingAfterDeletion(review);

                userReviewList.remove(reviewNumber - 1);
                displayUserReviews();
                Toast.makeText(this, "Review removed for update.", Toast.LENGTH_SHORT).show();

                // Start AddReviewActivity with existing review data
                Intent updateIntent = new Intent(PortfolioActivity.this, AddReviewActivity.class);
                updateIntent.putExtra("beachID", beachID);
                updateIntent.putExtra("userID", userID);
                updateIntent.putExtra("reviewID", review.getReviewID()); // [NEW] Pass reviewID for updating
                updateIntent.putExtra("reviewText", review.getText()); // [NEW] Pass existing review text
                updateIntent.putExtra("rating", review.getRating()); // [NEW] Pass existing rating
                if (review.getPictureUrls() != null && !review.getPictureUrls().isEmpty()) {
                    updateIntent.putExtra("pictureUrl", review.getPictureUrls().get(0)); // [NEW] Pass existing picture (assuming one photo)
                } else {
                    updateIntent.putExtra("pictureUrl", ""); // [NEW] No existing picture
                }
                updateIntent.putExtra("oldRating", oldRating); // [NEW] Pass old rating for accurate adjustment
                startActivity(updateIntent);
            } else {
                Toast.makeText(this, "Failed to remove review for update: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
                textViewUsername.setText("Error loading username in PortfolioActivity");
                textViewEmail.setText("Error loading email in PortfolioActivity");
            }
        });
    }

    private void fetchUserReviews() {
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
                    textViewNoReviews.setVisibility(View.VISIBLE); // [NEW]
                } else {
                    textViewNoReviews.setVisibility(View.GONE); // [NEW]
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
        pc = userReviewList.size();
        for (Review review : userReviewList) {
            String currentBeachID = review.getBeachID();
            if (beachNamesIDPair.containsKey(currentBeachID)) {
                pc -= 1;
                if (pc == 0) {
                    displayUserReviews();
                }
            } else {
                beachesRef.child(currentBeachID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String beachName = snapshot.child("name").getValue(String.class);
                        if (beachName == null) {
                            beachName = "beachName unknown/not found";
                        }
                        beachNamesIDPair.put(currentBeachID, beachName);
                        pc -= 1;
                        if (pc == 0) {
                            displayUserReviews();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        beachNamesIDPair.put(currentBeachID, "beachName unknown/not found");
                        pc -= 1;
                        if (pc == 0) {
                            displayUserReviews();
                        }
                    }
                });
            }
        }
    }

    /**
     * Displays user reviews by adding views to the reviewsContainer.
     * Handles reviews with and without pictureUrls.
     */
    public void displayUserReviews() {
        // Clear previous reviews
        reviewsContainer.removeAllViews();

        for (int i = 0; i < userReviewList.size(); i++) {
            Review review = userReviewList.get(i);
            String beachName = beachNamesIDPair.get(review.getBeachID());

            // Create a TextView for the review info
            TextView reviewInfo = new TextView(this);
            StringBuilder sb = new StringBuilder();
            sb.append("Review ").append(i + 1).append("\n");
            sb.append("Beach: ").append(beachName).append("\n");
            sb.append("Rating: ").append(review.getRating()).append(" ★\n");
            sb.append("Comment: ").append(review.getText()).append("\n");
            sb.append("------------------------------\n");
            reviewInfo.setText(sb.toString());

            // Optional: Add padding or styling to reviewInfo
            reviewInfo.setPadding(0, 8, 0, 8);
            reviewsContainer.addView(reviewInfo);

            // If review has pictureUrls, display them
            if (review.getPictureUrls() != null && !review.getPictureUrls().isEmpty()) {
                for (String photoName : review.getPictureUrls()) {
                    // Convert photoName to resource ID
                    int resId = getResources().getIdentifier(photoName, "drawable", getPackageName());
                    if (resId != 0) {
                        ImageView iv = new ImageView(this);
                        iv.setAdjustViewBounds(true);
                        iv.setMaxWidth(200);
                        iv.setMaxHeight(200);
                        iv.setImageResource(resId);
                        // Add some margin
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 8, 0, 8);
                        iv.setLayoutParams(lp);
                        reviewsContainer.addView(iv);
                    }
                }
            }
        }
    }

    /**
     * Updates the beach rating after deleting a review.
     *
     * @param delReview The review being deleted.
     */
    public void updateBeachRatingAfterDeletion(Review delReview) {
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
                    del_beachRef.setValue(b).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(PortfolioActivity.this, "Failed to update beach rating: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PortfolioActivity.this, "Failed to update beach rating: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
