package com.webdesign.countryservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webdesign.countryservice.model.ApiToken;
import com.webdesign.countryservice.model.User;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ApiTokenService {

    public String generateToken(String loginToken, String name, String expire) {
        User user = User.getUserByLoginToken(loginToken);
        if (user == null) return null;

        List<ApiToken> userApiTokens = user.getValidApiTokens();
        for (ApiToken apiToken : userApiTokens) {
            if (apiToken.getName().equals(name)) return null;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(expire);
        LocalDateTime expireDateTime = zonedDateTime.toLocalDateTime();

        ApiToken apiToken = new ApiToken(name, expireDateTime);
        user.addApiToken(apiToken);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", apiToken.getName());
        jsonObject.addProperty("expire_date", apiToken.getExpires().toString());
        jsonObject.addProperty("token", apiToken.getRawToken());

        return jsonObject.toString();
    }

    public String getUserTokens(String loginToken) {
        User owner = User.getUserByLoginToken(loginToken);
        if (owner == null) return null;

        List<ApiToken> apiTokens = owner.getValidApiTokens();
        JsonObject responseObject = reformatTokensList(apiTokens);
        return new Gson().toJson(responseObject);
    }

    public String revokeToken(String token) {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null) return null;

        if (!apiToken.isValid()) return null;
        apiToken.revoke();
        return "{\"success\": \"true\"";
    }


    private static JsonObject reformatTokensList(List<ApiToken> apiTokens) {
        JsonArray tokensArray = new JsonArray();

        for (ApiToken token : apiTokens) {
            JsonObject tokenObject = new JsonObject();
            tokenObject.addProperty("name", token.getName());
            tokenObject.addProperty("expire_date", token.getExpires().toString());
            tokenObject.addProperty("token", "API ***");
            tokensArray.add(tokenObject);
        }

        JsonObject responseObject = new JsonObject();
        responseObject.add("tokens", tokensArray);
        responseObject.addProperty("count", apiTokens.size());
        return responseObject;
    }
}
