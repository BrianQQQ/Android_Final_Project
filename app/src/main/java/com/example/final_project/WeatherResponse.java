package com.example.final_project;
public class WeatherResponse {
    private Location location;
    private Current current;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public static class Location {
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class Current {
        private String temperature;
        private String weather_descriptions;

        public String getTemperature() {
            return temperature;
        }

        public String getWeatherDescriptions() {
            return weather_descriptions;
        }
    }
}