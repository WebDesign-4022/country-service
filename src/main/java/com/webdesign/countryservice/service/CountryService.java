package com.webdesign.countryservice.service;

import com.webdesign.countryservice.model.Country;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final List<Country> countries = new ArrayList<>();

    public List<Country> getAllCountries() {
        return countries;
    }

    public Country getCountryByName(String name) {
        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }

    public void addCountry(Country country) {
        countries.add(country);
    }
}
