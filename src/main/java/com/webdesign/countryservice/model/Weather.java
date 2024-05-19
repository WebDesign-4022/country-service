package com.webdesign.countryservice.model;

public class Weather {
    private final String countryName;
    private final String capital;
    private float windSpeed;
    private int windDegrees;
    private int temp;
    private int humidity;

    public Weather(String countryName, String capital, float windSpeed, int windDegrees, int temp, int humidity) {
        this.countryName = countryName;
        this.capital = capital;
        this.windSpeed = windSpeed;
        this.windDegrees = windDegrees;
        this.temp = temp;
        this.humidity = humidity;
    }
}
