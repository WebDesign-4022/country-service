package com.webdesign.countryservice.service;

import com.webdesign.countryservice.model.Weather;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {
    private final List<Weather> weathers = new ArrayList<>();

    public Optional<Weather> getWeatherByCountryName(String countryName) {
        return weathers.stream().filter(weather -> weather.getCountryName().equalsIgnoreCase(countryName)).findFirst();
    }

    public void addWeather(Weather weather) {
        weathers.add(weather);
    }
}
