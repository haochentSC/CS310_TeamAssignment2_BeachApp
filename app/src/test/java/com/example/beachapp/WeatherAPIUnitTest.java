package com.example.beachapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WeatherAPIUnitTest {
    private static final String goodJSON = """
            {
              "data": {
                "time": "2024-11-21T10:09:00Z",
                "values": {
                  "cloudBase": 0.22,
                  "cloudCeiling": 0.22,
                  "cloudCover": 100,
                  "dewPoint": -2,
                  "freezingRainIntensity": 0,
                  "humidity": 88,
                  "precipitationProbability": 0,
                  "pressureSurfaceLevel": 981.29,
                  "rainIntensity": 0,
                  "sleetIntensity": 0,
                  "snowIntensity": 0,
                  "temperature": -0.19,
                  "temperatureApparent": -4.87,
                  "uvHealthConcern": 0,
                  "uvIndex": 0,
                  "visibility": 14.88,
                  "weatherCode": 1001,
                  "windDirection": 276.19,
                  "windGust": 9.38,
                  "windSpeed": 4.5
                }
              },
              "location": {
                "lat": 40.745491027832,
                "lon": -86.0639419555664,
                "name": "Miami County, Indiana, United States",
                "type": "administrative"
              }
            }""";

    //the parameter "windSpeed" is absent
    private static final String badJSON = """
            {
              "data": {
                "time": "2024-11-21T10:09:00Z",
                "values": {
                  "cloudBase": 0.22,
                  "cloudCeiling": 0.22,
                  "cloudCover": 100,
                  "dewPoint": -2,
                  "freezingRainIntensity": 0,
                  "humidity": 88,
                  "precipitationProbability": 0,
                  "pressureSurfaceLevel": 981.29,
                  "rainIntensity": 0,
                  "sleetIntensity": 0,
                  "snowIntensity": 0,
                  "temperature": -0.19,
                  "temperatureApparent": -4.87,
                  "uvHealthConcern": 0,
                  "uvIndex": 0,
                  "visibility": 14.88,
                  "weatherCode": 1001,
                  "windDirection": 276.19,
                  "windGust": 9.38,
                  "badWindSpeed": 4.5
                }
              },
              "location": {
                "lat": 40.745491027832,
                "lon": -86.0639419555664,
                "name": "Miami County, Indiana, United States",
                "type": "administrative"
              }
            }""";

    @Test
    public void testConstructURLValid() throws NoSuchFieldException, IllegalAccessException {
        double latitude = 34.05; // Los Angeles
        double longitude = -118.24;
        String infoType = "realtime";
        Field field = WeatherAPI.class.getDeclaredField("API_KEY");
        field.setAccessible(true);
        String API_key = (String)field.get(null);

        String expectedURL = "https://api.tomorrow.io/v4/weather/realtime?location=34.05, -118.24&apikey=" + API_key;

        String result = WeatherAPI.constructURL(infoType, latitude, longitude);
        assertEquals(result, expectedURL);
    }

    @Test
    public void testConstructURLBadInfoType() {
        double latitude = 34.05; // Los Angeles
        double longitude = -118.24;
        String badInfoType = "badInfoType";

        assertThrows(IllegalArgumentException.class, () ->
                WeatherAPI.constructURL(badInfoType, latitude, longitude));
    }

    @Test
    public void testConstructURLBadLongitude() {
        double latitude = 34.05;
        double longitude = -180.1; //bad longitude
        String badInfoType = "realtime";

        assertThrows(IllegalArgumentException.class, () ->
                WeatherAPI.constructURL(badInfoType, latitude, longitude));
    }

    @Test
    public void testExtractValsGoodJSON() {
        String parameter = "windSpeed";
        ArrayList<String> matches = WeatherAPI.extractVals(goodJSON, parameter);
        assertFalse(matches.isEmpty());
    }

    @Test
    public void testExtractValsBadJSON() {
        String parameter = "windSpeed";
        ArrayList<String> matches = WeatherAPI.extractVals(badJSON, parameter);
        assertTrue(matches.isEmpty());
    }
}