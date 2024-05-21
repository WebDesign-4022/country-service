package com.webdesign.countryservice.service;


import com.webdesign.countryservice.model.ApiToken;
import com.webdesign.countryservice.model.CacheEntry;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Service
public class CountryService {
    private static final Logger LOGGER = Logger.getLogger(CountryService.class.getName());
    private static final Level ERROR_LEVEL = Level.SEVERE;
    private static final Level INFO_LEVEL = Level.INFO;

    @Value("${apiKey}")
    private String apiKey;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public String getAllCountries(String token) {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            return null;
        }

        String cacheKey = "allCountries";
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://countriesnow.space/api/v0.1/countries")
                    .asString();
            LOGGER.log(new LogRecord(INFO_LEVEL, "All countries information received"));

            if (response.getStatus() != 200) throw new UnirestException(response.getBody());

            String reformattedData = reformatAllCountriesInfo(response.getBody());
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            LOGGER.log(new LogRecord(ERROR_LEVEL, "Error in getting all countries: " + e.getMessage()));
            return null;
        }
    }

    public String getCountryByName(String countryName, String token) {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            return null;
        }

        String cacheKey = "country_" + countryName;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://api.api-ninjas.com/v1/country?name=" + countryName)
                    .header("X-Api-Key", apiKey)
                    .asString();
            LOGGER.log(new LogRecord(INFO_LEVEL, "Country information received: " + countryName));

            if (response.getStatus() != 200) throw new UnirestException(response.getBody());

            String reformattedData = reformatCountryInfo(response.getBody());
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            LOGGER.log(new LogRecord(ERROR_LEVEL, "Error in getting country by name: " + e.getMessage()));
            return null;
        }
    }

    private String reformatAllCountriesInfo(String rawResponse) {
        JsonElement rootElement = JsonParser.parseString(rawResponse);
        JsonObject rootObject = rootElement.getAsJsonObject();

        JsonArray dataArray = rootObject.getAsJsonArray("data");
        JsonArray countriesArray = new JsonArray();
        for (JsonElement element : dataArray) {
            JsonObject countryObject = element.getAsJsonObject();
            String countryName = countryObject.get("country").getAsString();

            JsonObject newCountryObject = new JsonObject();
            newCountryObject.addProperty("name", countryName);
            countriesArray.add(newCountryObject);
        }

        JsonObject resultObject = new JsonObject();
        resultObject.add("countries", countriesArray);
        resultObject.addProperty("count", countriesArray.size());

        return resultObject.toString();
    }

    private String reformatCountryInfo(String rawResponse) {
        JsonElement rootElement = JsonParser.parseString(rawResponse);
        JsonArray jsonArray = rootElement.getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();

        JsonObject simplifiedObject = new JsonObject();
        simplifiedObject.addProperty("name", firstObject.get("name").getAsString().split(",")[0]); // "Iran, Islamic Republic Of" -> "Iran"
        simplifiedObject.addProperty("capital", firstObject.get("capital").getAsString());
        simplifiedObject.addProperty("iso2", firstObject.get("iso2").getAsString());
        simplifiedObject.addProperty("population", firstObject.get("population").getAsDouble());
        simplifiedObject.addProperty("pop_growth", firstObject.get("pop_growth").getAsDouble());
        simplifiedObject.add("currency", firstObject.get("currency").getAsJsonObject());

        return simplifiedObject.toString();
    }
}
