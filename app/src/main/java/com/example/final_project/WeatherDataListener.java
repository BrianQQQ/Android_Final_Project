package com.example.final_project;

public interface WeatherDataListener {
    void onDataReceived(WeatherData weatherData);
    void onError(String errorMessage);
}
