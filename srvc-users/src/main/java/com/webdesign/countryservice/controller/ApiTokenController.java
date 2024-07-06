package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.service.ApiTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class ApiTokenController {

    @Autowired
    private ApiTokenService apiTokenService;

    @PostMapping("/api-tokens")
    public ResponseEntity<String> generateToken(@RequestHeader("Authorization") String loginToken, @RequestBody Map<String, String> req) {
        try {
            String name = req.get("name");
            String expireDate = req.get("expire_date");
            String response = apiTokenService.generateToken(loginToken, name, expireDate);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/api-tokens")
    public ResponseEntity<String> getUserTokens(@RequestHeader("Authorization") String loginToken) {
        try {
            String response = apiTokenService.getUserTokens(loginToken);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/api-tokens")
    public ResponseEntity<String> revokeToken(@RequestHeader("Authorization") String apiToken) {
        try {
            String response = apiTokenService.revokeToken(apiToken);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
