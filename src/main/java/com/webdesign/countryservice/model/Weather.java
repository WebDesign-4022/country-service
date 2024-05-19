package com.webdesign.countryservice.model;

public class Weather {
    private String countryName;
    private String capital;
    private Double windSpeed;
    private Integer windDegrees;
    private Integer temp;
    private Integer humidity;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(Integer windDegrees) {
        this.windDegrees = windDegrees;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
}
