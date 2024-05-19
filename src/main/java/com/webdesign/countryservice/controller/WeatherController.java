package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.model.Weather;
import com.webdesign.countryservice.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/countries")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{name}/weather")
    public Optional<Weather> getWeatherByCountryName(@PathVariable String name) {
        return weatherService.getWeatherByCountryName(name);
    }

    @PostMapping("/{name}/weather")
    public void addWeather(@PathVariable String name, @RequestBody Weather weather) {
        weather.setCountryName(name);
        weatherService.addWeather(weather);
    }
}
