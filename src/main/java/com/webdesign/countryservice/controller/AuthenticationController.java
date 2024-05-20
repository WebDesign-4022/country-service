package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        String response = authenticationService.register(username, password);
        return Objects.requireNonNullElse(response, "Username already taken.");
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        String response = authenticationService.login(username, password);
        return Objects.requireNonNullElse(response, "Login failed. (check information or activate)");
    }
}
