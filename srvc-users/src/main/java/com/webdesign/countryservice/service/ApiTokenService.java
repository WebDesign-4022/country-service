package com.webdesign.countryservice.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.model.ApiToken;
import com.webdesign.countryservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ApiTokenService {

    public String generateToken(String loginToken, String name, String expire) throws HttpCustomException {
        User user = User.getUserByLoginToken(loginToken);
        if (user == null) {
            throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid login token");
        }

        ApiToken apiToken = createTokenObject(name, expire, user);
        user.addApiToken(apiToken);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", apiToken.getName());
        jsonObject.addProperty("expire_date", apiToken.getExpires().toString());
        jsonObject.addProperty("token", apiToken.getRawToken());

        return jsonObject.toString();
    }

    public String getUserTokens(String loginToken) throws HttpCustomException {
        User owner = User.getUserByLoginToken(loginToken);
        if (owner == null) {
            throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid login token");
        }

        List<ApiToken> apiTokens = owner.getValidApiTokens();
        JsonObject responseObject = reformatTokensList(apiTokens);
        return new Gson().toJson(responseObject);
    }

    public String revokeToken(String token) throws HttpCustomException {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        apiToken.revoke();
        return "{\"success\": \"true\"}";
    }

    public String validateToken(String token) throws HttpCustomException {
        ApiToken apiToken = ApiToken.getToken(token);
        if (apiToken == null || !apiToken.isValid()) {
            throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return "{\"valid\": \"true\"}";
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

    private static ApiToken createTokenObject(String name, String expire, User user) {
        List<ApiToken> userApiTokens = user.getValidApiTokens();
        for (ApiToken apiToken : userApiTokens) {
            if (apiToken.getName().equals(name)) {
                throw new HttpCustomException(HttpStatus.CONFLICT, "API token already exists");
            }
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(expire);
        LocalDateTime expireDateTime = zonedDateTime.toLocalDateTime();
        return new ApiToken(name, expireDateTime);
    }
}
