package com.webdesign.countryservice.controller;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/users")
    public ResponseEntity<String> activate(@RequestHeader("Authorization") String token, @RequestParam String username, @RequestParam boolean active) {
        try {
            String response = adminService.activate(username, active, token);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<String> usersList(@RequestHeader("Authorization") String token) {
        try {
            String response = adminService.listUsers(token);
            return ResponseEntity.ok(response);
        } catch (HttpCustomException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
