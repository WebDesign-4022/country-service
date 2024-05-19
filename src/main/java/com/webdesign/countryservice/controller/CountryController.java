package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.model.Country;
import com.webdesign.countryservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping
    public String getAllCountries() {
        String allCountries = countryService.getAllCountries();
        return Objects.requireNonNullElse(allCountries, "No countries found");
    }

    @GetMapping("/{name}")
    public Country getCountryByName(@PathVariable String name) {
        return null;
    }
}
