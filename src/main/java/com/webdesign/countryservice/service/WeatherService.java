package com.webdesign.countryservice.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.webdesign.countryservice.model.ApiToken;
import com.webdesign.countryservice.model.CacheEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
public class WeatherService {
    private static final Logger LOGGER = Logger.getLogger(WeatherService.class.getName());
    private static final Level ERROR_LEVEL = Level.SEVERE;
    private static final Level INFO_LEVEL = Level.INFO;

    @Value("${apiKey}")
    private String apiKey;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Autowired
    private CountryService countryService;

    public String getWeatherByCountryName(String countryName, String token) {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            return null;
        }

        String cacheKey = "weather_" + countryName;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.getData();
        }

        String countryInfo = countryService.getCountryByName(countryName, token);
        if (countryInfo == null) return null;

        JsonElement rootElement = JsonParser.parseString(countryInfo);
        JsonObject jsonObject = rootElement.getAsJsonObject();
        String capitalName = jsonObject.get("capital").getAsString();

        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("https://api.api-ninjas.com/v1/weather?city=" + capitalName)
                    .header("X-Api-Key", apiKey)
                    .asString();
            LOGGER.log(new LogRecord(INFO_LEVEL, "Weather information received: " + capitalName));

            if (response.getStatus() != 200) throw new UnirestException(response.getBody());
            String reformattedData = reformatWeatherInfo(response.getBody(), countryName, capitalName);
            cache.put(cacheKey, new CacheEntry(reformattedData, LocalDateTime.now().plusHours(1)));
            return reformattedData;

        } catch (UnirestException e) {
            LOGGER.log(new LogRecord(ERROR_LEVEL, "Error in getting weather by city name: " + e.getMessage()));
            return null;
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
