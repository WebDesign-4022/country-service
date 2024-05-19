package com.webdesign.countryservice.controller;

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
        String allCountriesInfo = countryService.getAllCountries();
        return Objects.requireNonNullElse(allCountriesInfo, "Can not get countries information.");
    }

    @GetMapping("/{name}")
    public String getCountryByName(@PathVariable String name) {
        String countryInfo = countryService.getCountryByName(name);
        return Objects.requireNonNullElse(countryInfo, "Can not get country information.");
    }
}
