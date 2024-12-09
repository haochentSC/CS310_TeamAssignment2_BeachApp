package com.example.beachapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class DisplayBeachActivity extends AppCompatActivity implements WeatherAPI.WeatherCallback {
    private ImageView imageViewBeachPhoto;
    private TextView textViewBeachName;
    private TextView textViewAccessHours;
    private TextView textViewWeatherTemperature;
    private TextView textViewWeatherWindSpeed;
    private TextView textViewWaveHeight;
    private TableLayout forecastTable;
    private Button buttonRating;
    private Button buttonPortfolio;
    private String beachID;
    private String userID;
    private ExecutorService executorService;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference_beach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_beach);
        imageViewBeachPhoto= findViewById(R.id.imageViewBeachPhoto);
        textViewBeachName= findViewById(R.id.textViewBeachName);
        textViewAccessHours = findViewById(R.id.textViewAccessHours);
        textViewWeatherTemperature = findViewById(R.id.weatherTemperature);
        textViewWeatherWindSpeed = findViewById(R.id.weatherWindSpeed);
        textViewWaveHeight = findViewById(R.id.waveHeight);
        forecastTable = findViewById(R.id.forecastTable);
        buttonRating = findViewById(R.id.buttonRating);
        buttonPortfolio = findViewById(R.id.buttonPortfolio);
        Intent intent=getIntent();
        if (intent != null) {
            beachID = intent.getStringExtra("beachID");
            userID = intent.getStringExtra("userID");
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_beach = firebaseDatabase.getReference("beaches");

        fetchBeachData();

        displayTopTwoActivities(beachID);

        buttonRating.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(DisplayBeachActivity.this, ReviewActivity.class);
            reviewIntent.putExtra("beachID", beachID);
            reviewIntent.putExtra("userID", userID);
            startActivity(reviewIntent);
        });
        buttonPortfolio.setOnClickListener(v -> {
            Intent intentPortfolio = new Intent(DisplayBeachActivity.this, PortfolioActivity.class);
            intentPortfolio.putExtra("beachID", beachID);
            intentPortfolio.putExtra("userID", userID);
            startActivity(intentPortfolio);
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
                    WeatherAPI.fetchWeather(beach.getLatitude(), beach.getLongitude(), DisplayBeachActivity.this);
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

    @Override
    public void onWeatherDataFetched(double temperature, double windSpeed, double waveHeight, ArrayList<Double> forecastTemps) {
        textViewWeatherTemperature.setText("Temperature: " + temperature + "°C");
        textViewWeatherWindSpeed.setText("Wind speed: " + windSpeed + " mph");
        textViewWaveHeight.setText("Max wave height: " + waveHeight + " meters");

        // Provide a max of 8 successive hourly updates to temperature
        TableRow row = (TableRow) forecastTable.getChildAt(0);
        for (int i = 0; i < 8; i++) {
            TextView cell = (TextView)row.getChildAt(i);
            cell.setText(String.valueOf(forecastTemps.get(i)));
        }
    }

    private void displayTopTwoActivities(String beachName) {
        try {
            JSONObject beachActivitiesJson = loadBeachActivitiesJson();
            if (beachActivitiesJson != null) {
                JSONObject activities = beachActivitiesJson.getJSONObject(beachName);

                Map<String, Integer> activityMap = new HashMap<>();
                Iterator<String> keys = activities.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    activityMap.put(key, activities.getInt(key));
                }

                List<Map.Entry<String, Integer>> sortedActivities = new ArrayList<>(activityMap.entrySet());
                sortedActivities.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));

                String topActivity1 = sortedActivities.size() > 0 ? sortedActivities.get(0).getKey() : "N/A";
                String topActivity2 = sortedActivities.size() > 1 ? sortedActivities.get(1).getKey() : "N/A";

                // Display them
                TextView topActivityTextView = findViewById(R.id.topActivitiesTextView); // Assuming you have this TextView
                StringBuilder topActivitiesText = new StringBuilder();
                topActivitiesText.append("Top two activities:\n");
                topActivitiesText.append("1. ").append(topActivity1).append(": ").append(sortedActivities.size() > 0 ? sortedActivities.get(0).getValue() : 0).append("\n");

                topActivitiesText.append("2. ").append(topActivity2).append(": ").append(sortedActivities.size() > 1 ? sortedActivities.get(1).getValue() : 0);

                topActivityTextView.setText(topActivitiesText.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject loadBeachActivitiesJson() {
        File file = new File(getFilesDir(), "activity_tag.json");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonContent = stringBuilder.toString();
            return new JSONObject(jsonContent);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
