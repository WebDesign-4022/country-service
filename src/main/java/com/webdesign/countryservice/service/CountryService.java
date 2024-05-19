package com.webdesign.countryservice.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Service
public class CountryService {
    private static final Logger LOGGER = Logger.getLogger(CountryService.class.getName());
    private static final Level ERROR_LEVEL = Level.SEVERE;
    private static final Level INFO_LEVEL = Level.INFO;

    public String getAllCountries() {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://countriesnow.space/api/v0.1/countries").asString();
            LOGGER.log(new LogRecord(INFO_LEVEL, "Countries list received"));
            return response.getBody();
        } catch (UnirestException e) {
            LOGGER.log(new LogRecord(ERROR_LEVEL, "Error in getting all countries list: " + e.getMessage()));
            return null;
        }
    }
}
