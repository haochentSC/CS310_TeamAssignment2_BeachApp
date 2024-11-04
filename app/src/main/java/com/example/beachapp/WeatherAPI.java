package com.example.beachapp;

import java.net.*;
import java.io.*;

public class WeatherAPI {
    static String API_KEY = "EQ2Mscv6d31ynkew7peprkmoTegJ5fjW";

    public static String fetchWeather(double latitude, double longitude) throws Exception {
        String urlBase = "https://api.tomorrow.io/v4/weather/realtime?";
        String urlParams = "location=" + latitude + ", " + longitude + "&apikey=" + API_KEY;
        URL url = new URL(urlBase + urlParams);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}