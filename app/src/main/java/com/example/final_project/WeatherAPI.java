
package com.example.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAPI {

    private static final String API_KEY = "6148eaa7dc6ad9bebd30a7f95158d62f";
    private static final String BASE_URL = "http://api.weatherstack.com/current?access_key=" + API_KEY + "&query=";

    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    String iconUrl;
   /* private Gson gson;
Context context;
    public WeatherAPI() {
        requestQueue = Volley.newRequestQueue(context);
        gson = new GsonBuilder().create();
    }

    public void getWeatherByCity(String city, final WeatherCallback callback) {
        String url = BASE_URL + city;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);
                        callback.onSuccess(weatherResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });

        requestQueue.add(stringRequest);
    }

    public interface WeatherCallback {
        void onSuccess(WeatherResponse response);
        void onError(String message);
    }*/



    public WeatherAPI(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void getWeatherDataForCity(String cityName, WeatherDataListener listener) {
//        String url = BASE_URL + "?access_key=" + API_KEY + "&query=" + cityName;
        String url = BASE_URL + cityName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response and retrieve the city name and weather data
                            JSONObject location = response.getJSONObject("location");
                            String cityName = location.getString("name");

                            JSONObject current = response.getJSONObject("current");
                            double temperature = current.getDouble("temperature");
                            JSONArray weatherDescriptions = current.getJSONArray("weather_descriptions");
                            String weatherCondition = weatherDescriptions.getString(0);

//                            String weatherCondition = current.getJSONObject("weather_descriptions").getString(String.valueOf(0));
                            iconUrl = current.getJSONArray("weather_icons").getString(0);                            // Create a new WeatherData object with the retrieved data
//                            WeatherData weatherData = new WeatherData(cityName, temperature, weatherCondition);
                            WeatherData weatherData = new WeatherData(cityName, temperature, weatherCondition, iconUrl);

                            // Save the weather data in SharedPreferences
                            saveWeatherData(weatherData);

                            // Notify the listener that the weather data was received
                            listener.onDataReceived(weatherData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                           listener.onError("Failed to retrieve weather data for " + cityName);
                            /*if (listener != null) {
                                listener.onError("Failed to retrieve weather data for " + cityName);
                            }*/
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError("Failed to retrieve weather data for " + cityName);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveWeatherData(WeatherData weatherData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cityName", weatherData.getCityName());
        editor.putFloat("temperature", (float) weatherData.getTemperature());
        editor.putString("weatherCondition", weatherData.getWeatherCondition());
        editor.putString("iconUrl", weatherData.getWeatherIconUrl());

        editor.apply();
    }

    public WeatherData getSavedWeatherData() {
        String cityName = sharedPreferences.getString("cityName", null);
        float temperature = sharedPreferences.getFloat("temperature", Float.NaN);
        String weatherCondition = sharedPreferences.getString("weatherCondition", null);
        String iconUrl = sharedPreferences.getString("iconUrl", null);

        if (cityName == null || Float.isNaN(temperature) || weatherCondition == null || iconUrl == null) {
            return null;
        } else {
            return new WeatherData(cityName, temperature, weatherCondition, iconUrl);
        }
    }

}
