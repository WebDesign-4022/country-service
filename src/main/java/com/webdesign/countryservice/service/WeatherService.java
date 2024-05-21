package com.webdesign.countryservice.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.model.ApiToken;
import com.webdesign.countryservice.model.CacheEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class WeatherService {

    @Value("${apiKey}")
    private String apiKey;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Autowired
    private CountryService countryService;

    public String getWeatherByCountryName(String countryName, String token) throws HttpCustomException {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid api token");
        }

        String cacheKey = "weather_" + countryName;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        String countryInfo = countryService.getCountryByName(countryName, token);

        JsonElement rootElement = JsonParser.parseString(countryInfo);
        JsonObject jsonObject = rootElement.getAsJsonObject();
        String capitalName = jsonObject.get("capital").getAsString();

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://api.api-ninjas.com/v1/weather?city=" + capitalName).header("X-Api-Key", apiKey).asString();

            if (response.getStatus() != 200) {
                throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not connect to external service");
            }
            String reformattedData = reformatWeatherInfo(response.getBody(), countryName, capitalName);
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in getting weather by city name: " + e.getMessage());
        }
    }


    private String reformatWeatherInfo(String weatherInfo, String countryName, String capitalName) {
        JsonObject weatherData = JsonParser.parseString(weatherInfo).getAsJsonObject();
        JsonObject formattedJson = new JsonObject();

        formattedJson.addProperty("country_name", countryName);
        formattedJson.addProperty("capital", capitalName);

        formattedJson.addProperty("wind_speed", weatherData.get("wind_speed").getAsDouble());
        formattedJson.addProperty("wind_degrees", weatherData.get("wind_degrees").getAsInt());
        formattedJson.addProperty("temp", weatherData.get("temp").getAsInt());
        formattedJson.addProperty("humidity", weatherData.get("humidity").getAsInt());

        return formattedJson.toString();
    }
}
