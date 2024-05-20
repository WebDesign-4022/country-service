package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/users")
    public String activate(@RequestHeader("Authorization") String token, @RequestParam String username, @RequestParam boolean active) {
        String response = adminService.activate(username, active, token);
        return Objects.requireNonNullElse(response, "Can not change active status (wrong token or wrong username).");
    }

    @GetMapping("/users")
    public String usersList(@RequestHeader("Authorization") String token) {
        String response = adminService.listUsers(token);
        return Objects.requireNonNullElse(response, "Wrong admin token.");
    }
}
