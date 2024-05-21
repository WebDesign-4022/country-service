package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<String> getAllCountries(@RequestHeader("Authorization") String apiToken) {
        try {
            String allCountriesInfo = countryService.getAllCountries(apiToken);
            return ResponseEntity.ok(allCountriesInfo);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getCountryByName(@RequestHeader("Authorization") String apiToken, @PathVariable String name) {
        try {
            String countryInfo = countryService.getCountryByName(name, apiToken);
            return ResponseEntity.ok(countryInfo);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
