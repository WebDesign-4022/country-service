package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> req) {
        try {
            String username = req.get("username");
            String password = req.get("password");
            String response = authenticationService.register(username, password);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> req) {
        try {
            String username = req.get("username");
            String password = req.get("password");
            String response = authenticationService.login(username, password);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
