package com.example.beachapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAPI {
    private final static String API_KEY = "EQ2Mscv6d31ynkew7peprkmoTegJ5fjW";
    private static String realtimeJSON;
    private static String forecastJSON;
    private static String waveJSON;
    private static double temperature;
    private static double windSpeed;
    private static ArrayList<Double> forecastTemperatureArray;
    private static double waveHeight;

    public static void fetchWeather(double latitude, double longitude) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                realtimeJSON = WeatherAPI.fetchWeatherHelper("realtime", latitude, longitude);
                forecastJSON = WeatherAPI.fetchWeatherHelper("forecast", latitude, longitude);
                waveJSON = WeatherAPI.fetchWeatherHelper("marine", latitude, longitude);
                parseJSON();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    private static String fetchWeatherHelper(String infoType, double latitude, double longitude) throws Exception {
        URL url = new URL(constructURL(infoType, latitude, longitude));
        System.out.println(url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static String constructURL(String infoType, double latitude, double longitude) {
        String weatherBase = "https://api.tomorrow.io/v4/weather/";
        String waveBase = "https://marine-api.open-meteo.com/v1/";
        String urlBase;
        if(infoType.equals("marine")) {
            urlBase = waveBase;
        }
        else {
            urlBase = weatherBase;
        }
        String urlParams = infoType;
        if(infoType.equals("marine")) {
            urlParams += "?latitude=" + latitude + "&longitude=" + longitude + "&current=wave_height";
        }
        else {
            urlParams += "?location=" + latitude + ", " + longitude;
            if(infoType.equals("forecast")) {
                urlParams += "&timesteps=1d";
            }
            urlParams += "&apikey=" + API_KEY;
        }
        return urlBase + urlParams;
    }

    private static void parseJSON() {
        /* Find real time temperature (in Celsius) and wind speed (in mph) */
        ArrayList<String> tempList = extractVals(realtimeJSON, "temperature");
        ArrayList<String> windList = extractVals(realtimeJSON, "windSpeed");
        temperature = Double.parseDouble(tempList.get(0));
        windSpeed = Double.parseDouble(windList.get(0));

        /* Find forecast temps over a week (in Celsius) */
        ArrayList<String> forecastTemps = extractVals(forecastJSON, "temperatureMax");
        for(int i = 0; i < forecastTemps.size(); i++) {
            forecastTemperatureArray.add(Double.parseDouble(forecastTemps.get(i)));
        }

        /* Find real time wave height (in meters)*/
        ArrayList<String> waveList = extractVals(waveJSON, "wave_height");
        waveHeight = Double.parseDouble(waveList.get(0));
    }

    private static ArrayList<String> extractVals(String jsonText, String parameter) {
        ArrayList<String> allMatches = new ArrayList<>();
        String tempRegex = "\"" + parameter + "\":(\\d+(\\.\\d+)?)";
        Pattern pattern = Pattern.compile(tempRegex);
        Matcher matcher = pattern.matcher(jsonText);
        while(matcher.find()) {
            allMatches.add(matcher.group(0));
        }
        return allMatches;
    }

    // Methods to obtain temp (Celsius), wind speed (mph),
    // forecast temps (Celsius), and wave height (meters)

    public static double getTemperature() {
        return temperature;
    }

    public static double getWindSpeed() {
        return windSpeed;
    }

    public static ArrayList<Double> getForecastTempArray() {
        return forecastTemperatureArray;
    }

    public static double getWaveHeight() {
        return waveHeight;
    }
}