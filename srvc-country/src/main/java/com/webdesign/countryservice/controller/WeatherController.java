package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{name}/weather")
    public ResponseEntity<String> getWeatherByCountryName(@RequestHeader("Authorization") String apiToken, @PathVariable String name) {
        try {
            String capitalWeather = weatherService.getWeatherByCountryName(name, apiToken);
            return ResponseEntity.ok(capitalWeather);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
