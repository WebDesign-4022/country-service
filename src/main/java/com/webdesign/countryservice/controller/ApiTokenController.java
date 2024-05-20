package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.service.ApiTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/user")
public class ApiTokenController {

    @Autowired
    private ApiTokenService apiTokenService;

    @PostMapping("/api-tokens")
    public String generateToken(@RequestHeader("Authorization") String loginToken, @RequestBody Map<String, String> req) {
        String name = req.get("name");
        String expireDate = req.get("expire_date");
        String response = apiTokenService.generateToken(loginToken, name, expireDate);
        return Objects.requireNonNullElse(response, "Can not generate token (wrong login token or duplicated token).");
    }

    @GetMapping("/api-tokens")
    public String getUserTokens(@RequestHeader("Authorization") String loginToken) {
        String response = apiTokenService.getUserTokens(loginToken);
        return Objects.requireNonNullElse(response, "Invalid login token.");
    }

    @DeleteMapping("/api-tokens")
    public String revokeToken(@RequestHeader("Authorization") String apiToken) {
        String response = apiTokenService.revokeToken(apiToken);
        return Objects.requireNonNullElse(response, "Invalid api token or token already revoked.");
    }
}
