package com.example.final_project;
public class WeatherData {
    private String cityName;
    private double temperature;
    private String weatherCondition;
    private String weatherIconUrl;
    public WeatherData(String cityName, double temperature, String weatherCondition, String weatherIconUrl) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherIconUrl = weatherIconUrl;
    }
    public WeatherData(String cityName, double temperature, String weatherCondition) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;

    }
    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    public void setWeatherIconUrl(String weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }
    public String getWeatherIconUrl() { return weatherIconUrl; }
}

