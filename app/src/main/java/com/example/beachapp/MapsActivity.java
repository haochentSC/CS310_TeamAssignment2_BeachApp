package com.example.beachapp;

import androidx.fragment.app.FragmentActivity;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.beachapp.databinding.ActivityMapsBinding;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    private UiSettings mUiSettings;
    private ActivityMapsBinding binding;
    private Marker SM;
    private Marker MB;
    private Marker AB;
    public Marker HB;
    private Marker NB;

    class CustomInfoWindowAdapter implements InfoWindowAdapter {
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null; // Use getInfoWindow for custom layout
        }

        private void render(Marker marker, View view) {
            String title = marker.getTitle(); // Retrieve the title from the marker
            TextView titleUi = view.findViewById(R.id.title);
            if (title != null && !title.isEmpty()) {
                titleUi.setText(title);
            } else {
                titleUi.setText("Unknown Location");
            }

            Button seeMoreButton = view.findViewById(R.id.redirect);
            seeMoreButton.setText("See More");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void moveCameraToMarker(Marker marker, LatLng location, String title) {
        // Move the camera to the selected marker and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 9.6f));

        // Move the camera and set the marker to show its info window
        marker.showInfoWindow();  // This will show the info window of the selected marker
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));

        // Initialize the custom info window adapter
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Define locations with custom titles
        LatLng santamonica = new LatLng(34.01943, -118.48968);
        LatLng manhattan = new LatLng(33.88477, -118.41103);
        LatLng alamitos = new LatLng(33.763, -118.1749);
        LatLng huntington = new LatLng(33.65953, -117.99984);
        LatLng newport = new LatLng(33.60493, -117.87487);

        SM = mMap.addMarker(new MarkerOptions().position(santamonica).title("Santa Monica Beach"));
        SM.setTag("beach001");
        MB = mMap.addMarker(new MarkerOptions().position(manhattan).title("Manhattan Beach"));
        MB.setTag("beach002");
        AB = mMap.addMarker(new MarkerOptions().position(alamitos).title("Alamitos Beach"));
        AB.setTag("beach003");
        HB =mMap.addMarker(new MarkerOptions().position(huntington).title("Huntington Beach"));
        HB.setTag("beach004");
        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
        NB.setTag("beach005");


        Spinner markerSpinner = findViewById(R.id.markerSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {
                "All Beaches",
                "Swimming",
                "Surfing",
                "Biking",
                "Volleyball",
                "Snorkeling"
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        markerSpinner.setAdapter(adapter);

        // Set the listener for Spinner item selection
        markerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0: // All Beaches
                        mMap.clear();
                        SM = mMap.addMarker(new MarkerOptions().position(santamonica).title("Santa Monica Beach"));
                        SM.setTag("beach001");
                        MB = mMap.addMarker(new MarkerOptions().position(manhattan).title("Manhattan Beach"));
                        MB.setTag("beach002");
                        AB = mMap.addMarker(new MarkerOptions().position(alamitos).title("Alamitos Beach"));
                        AB.setTag("beach003");
                        HB =mMap.addMarker(new MarkerOptions().position(huntington).title("Huntington Beach"));
                        HB.setTag("beach004");
                        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
                        NB.setTag("beach005");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                    case 1: // Swimming
                        mMap.clear();
                        SM = mMap.addMarker(new MarkerOptions().position(santamonica).title("Santa Monica Beach"));
                        SM.setTag("beach001");
                        MB = mMap.addMarker(new MarkerOptions().position(manhattan).title("Manhattan Beach"));
                        MB.setTag("beach002");
                        HB =mMap.addMarker(new MarkerOptions().position(huntington).title("Huntington Beach"));
                        HB.setTag("beach004");
                        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
                        NB.setTag("beach005");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                    case 2: // Surfing
                        mMap.clear();
                        SM = mMap.addMarker(new MarkerOptions().position(santamonica).title("Santa Monica Beach"));
                        SM.setTag("beach001");
                        MB = mMap.addMarker(new MarkerOptions().position(manhattan).title("Manhattan Beach"));
                        MB.setTag("beach002");
                        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
                        NB.setTag("beach005");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                    case 3: // Biking
                        mMap.clear();
                        AB = mMap.addMarker(new MarkerOptions().position(alamitos).title("Alamitos Beach"));
                        AB.setTag("beach003");
                        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
                        NB.setTag("beach005");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                    case 4: // Volleyball
                        mMap.clear();
                        SM = mMap.addMarker(new MarkerOptions().position(santamonica).title("Santa Monica Beach"));
                        SM.setTag("beach001");
                        NB = mMap.addMarker(new MarkerOptions().position(newport).title("Newport Beach"));
                        NB.setTag("beach005");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                    case 5: // Snorkeling
                        mMap.clear();
                        MB = mMap.addMarker(new MarkerOptions().position(manhattan).title("Manhattan Beach"));
                        MB.setTag("beach002");
                        HB =mMap.addMarker(new MarkerOptions().position(huntington).title("Huntington Beach"));
                        HB.setTag("beach004");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8, -118.19), 9.6f));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });


        mMap.setOnInfoWindowClickListener(marker -> {
            String userID;
            String selectedBeachID = (String) marker.getTag();
            Intent intent=getIntent();
            if (intent != null) {
                userID = intent.getStringExtra("userID");
                Intent intent2 = new Intent(MapsActivity.this, DisplayBeachActivity.class);
                intent2.putExtra("beachID", selectedBeachID);
                intent2.putExtra("userID", userID);
                startActivity(intent2);
            }
        });
    }
}
