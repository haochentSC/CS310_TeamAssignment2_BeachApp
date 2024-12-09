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

/* WeatherAPI is static, so don't need to instantiate the class */
/* Ex:   WeatherAPI.fetchWeather(LATITUDE, LONGITUDE);          */
/*       double temp = WeatherAPI.getTemperature()              */

public class WeatherAPI {
    //private final static String API_KEY = "jXC2DReCT7GZAZCmebno6qGLsgCRd1EK";
    private final static String API_KEY = "EQ2Mscv6d31ynkew7peprkmoTegJ5fjW";
    private static String realtimeJSON;
    private static String forecastJSON;
    private static String waveJSON;
    private static double temperature;
    private static double windSpeed;
    private static ArrayList<Double> forecastTemperatureArray;
    private static double waveHeight;

    public interface WeatherCallback {
        void onWeatherDataFetched(double temperature, double windSpeed, double waveHeight, ArrayList<Double> forecastTemps);
    }

    public static void fetchWeather(double latitude, double longitude, WeatherCallback weatherCallback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                realtimeJSON = WeatherAPI.fetchWeatherHelper("realtime", latitude, longitude);
                forecastJSON = WeatherAPI.fetchWeatherHelper("forecast", latitude, longitude);
                waveJSON = WeatherAPI.fetchWeatherHelper("marine", latitude, longitude);
                parseJSON(realtimeJSON, forecastJSON, waveJSON);

                // Run callback on the main UI thread to update the UI
                if (weatherCallback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            weatherCallback.onWeatherDataFetched(temperature, windSpeed, waveHeight, forecastTemperatureArray)
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        });
    }

    private static String fetchWeatherHelper(String infoType, double latitude, double longitude) throws Exception {
        URL url = new URL(constructURL(infoType, latitude, longitude));
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

    protected static String constructURL(String infoType, double latitude, double longitude) throws IllegalArgumentException {
        String weatherBase = "https://api.tomorrow.io/v4/weather/";
        String waveBase = "https://marine-api.open-meteo.com/v1/";
        String urlBase;

        if(infoType.equals("marine")) {
            urlBase = waveBase;
        } else if (infoType.equals("realtime") || infoType.equals("forecast")) {
            urlBase = weatherBase;
        } else {
            throw new IllegalArgumentException("Invalid infoType: " + infoType);
        }

        if(latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid latitude: " + latitude);
        }
        if(longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Invalid longitude: " + longitude);
        }

        String urlParams = infoType;
        if(infoType.equals("marine")) {
            urlParams += "?latitude=" + latitude + "&longitude=" + longitude + "&current=wave_height";
        }
        else {
            urlParams += "?location=" + latitude + ", " + longitude;
            if (infoType.equals("forecast")) {
                // grab every hour change
                urlParams += "&timesteps=1h";
            }
            urlParams += "&apikey=" + API_KEY;
        }
        return urlBase + urlParams;
    }

    public static void parseJSON(String realtimeInput, String forecastInput, String waveInput) {
        try {
            if (forecastTemperatureArray == null) {
                forecastTemperatureArray = new ArrayList<>();
            } else {
                forecastTemperatureArray.clear();
            }

            /* Extract real-time temperature and wind speed */
            ArrayList<String> tempList = extractVals(realtimeInput, "temperature");
            ArrayList<String> windList = extractVals(realtimeInput, "windSpeed");
            temperature = Double.parseDouble(tempList.get(0));
            windSpeed = Double.parseDouble(windList.get(0));

            /* Extract forecast temperatures */
            ArrayList<String> forecastTemps = extractVals(forecastInput, "temperature");
            for(int i = 0; i < 10; i++) {
                forecastTemperatureArray.add(Double.parseDouble(forecastTemps.get(i)));
            }

            /* Extract wave height */
            ArrayList<String> waveList = extractVals(waveInput, "wave_height");
            waveHeight = Double.parseDouble(waveList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> extractVals(String jsonText, String parameter) {
        ArrayList<String> allMatches = new ArrayList<>();
        String tempRegex = "\"" + parameter + "\":\\s*(-?\\d+(\\.\\d+)?)";
        Pattern pattern = Pattern.compile(tempRegex);
        Matcher matcher = pattern.matcher(jsonText);
        while(matcher.find()) {
            allMatches.add(matcher.group(1));
        }
        return allMatches;
    }
}