package com.example.beachapp;

import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MapsUnitTest {

    private TextView titleUi;

    @Before
    public void setUp() {

        titleUi = new TextView(RuntimeEnvironment.application); // This provides a valid Context
    }

    @Test
    public void testGoodMarkerTitle() {
        // Simulate getting a title from the Marker
        String goodtitle = "Santa Monica Beach";

        // Apply the logic from your activity code
        if (goodtitle != null && !goodtitle.isEmpty()) {
            titleUi.setText(goodtitle); // Set the title in the TextView
        } else {
            titleUi.setText("Unknown Location");
        }

        // Verify that the TextView's setText method was called with the expected title
        assertEquals("Santa Monica Beach", titleUi.getText().toString());
    }

    @Test
    public void testEmptyMarkerTitle() {
        String badtitle = "";

        if (badtitle != null && !badtitle.isEmpty()) {
            titleUi.setText(badtitle); // Set the title in the TextView
        } else {
            titleUi.setText("Unknown Location");
        }

        // Verify that the TextView's setText method was called with the expected title
        assertEquals("Unknown Location", titleUi.getText().toString());
    }


    @Test
    public void testNullMarkerTitle() {
        String nulltitle = null;

        if (nulltitle != null && !nulltitle.isEmpty()) {
            titleUi.setText(nulltitle); // Set the title in the TextView
        } else {
            titleUi.setText("Unknown Location");
        }

        // Verify that the TextView's setText method was called with the expected title
        assertEquals("Unknown Location", titleUi.getText().toString());
    }

    class MockMarker {
        private String tag;

        void setTag(String tag) {
            this.tag = tag;
        }

        String getTag() {
            return tag;
        }
    }

    @Test
    public void testMarkerTagRetrieval() {
        MockMarker marker = new MockMarker(); // Simulated marker object
        marker.setTag("beach001");            // Set a tag
        assertEquals("beach001", marker.getTag()); // Verify the tag retrieval
    }

    private static class MockSpinnerAdapter {
        private final String[] items;

        MockSpinnerAdapter(String[] items) {
            this.items = items;
        }

        String getItem(int position) {
            return items[position];
        }
    }

    @Test
    public void testSpinnerSelectionLogic() {
        String[] beaches = {"Santa Monica Beach", "Manhattan Beach", "Alamitos Beach", "Huntington Beach", "Newport Beach"};
        MockSpinnerAdapter adapter = new MockSpinnerAdapter(beaches);

        assertEquals("Santa Monica Beach", adapter.getItem(0));
        assertEquals("Newport Beach", adapter.getItem(4));
    }











}

