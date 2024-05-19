package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/countries")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{name}/weather")
    public String getWeatherByCountryName(@PathVariable String name) {
        String capitalWeather = weatherService.getWeatherByCountryName(name);
        return Objects.requireNonNullElse(capitalWeather, "Can not get country weather information.");
    }
}
