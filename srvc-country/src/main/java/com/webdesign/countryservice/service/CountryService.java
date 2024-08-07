package com.webdesign.countryservice.service;


import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.model.CacheEntry;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.webdesign.countryservice.service.ExternalService.authorizeToken;

@Service
public class CountryService {

    @Value("${apiKey}")
    private String apiKey;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public String getAllCountries(String apiToken) throws HttpCustomException {
        authorizeToken(apiToken);

        String cacheKey = "allCountries";
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://countriesnow.space/api/v0.1/countries").asString();

            if (response.getStatus() != 200) {
                throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not connect to external service");
            }

            String reformattedData = reformatAllCountriesInfo(response.getBody());
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in getting all countries: " + e.getMessage());
        }
    }

    public String getCountryByName(String countryName, String token) throws HttpCustomException {
        authorizeToken(token);

        String cacheKey = "country_" + countryName;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://api.api-ninjas.com/v1/country?name=" + countryName).header("X-Api-Key", apiKey).asString();

            if (response.getStatus() != 200) {
                throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not connect to external service");
            }

            if (response.getBody().length() < 3) {
                throw new HttpCustomException(HttpStatus.NOT_FOUND, "Can not find country with name " + countryName);
            }

            String reformattedData = reformatCountryInfo(response.getBody());
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in getting country by name: " + e.getMessage());
        }
    }

    public String getCityCoordination(String cityName) throws HttpCustomException {
        String cacheKey = "cityCoordination_" + cityName;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://api.api-ninjas.com/v1/city?name=" + cityName).header("X-Api-Key", apiKey).asString();

            if (response.getStatus() != 200) {
                throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not connect to external service");
            }

            if (response.getBody().length() < 3) {
                throw new HttpCustomException(HttpStatus.NOT_FOUND, "Can not find city with name " + cityName);
            }

            String coordination = extractCityCoordination(response.getBody());
            cache.put(cacheKey, new CacheEntry(coordination, LocalDateTime.now().plusHours(1)));
            return coordination;

        } catch (UnirestException e) {
            throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in getting country by name: " + e.getMessage());
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

    private String extractCityCoordination(String rawResponse) {
        JsonElement rootElement = JsonParser.parseString(rawResponse);
        JsonArray jsonArray = rootElement.getAsJsonArray();
        JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
        return firstObject.get("latitude").getAsDouble() + "," + firstObject.get("longitude").getAsDouble();
    }
}
