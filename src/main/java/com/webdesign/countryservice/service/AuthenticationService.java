package com.webdesign.countryservice.service;

import com.webdesign.countryservice.model.User;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    public String register(String username, String password) {
        if (User.getUserByName(username) != null) {
            return null;
        }

        new User(username, password);
        return "{\"success\": true}";
    }

    public String login(String username, String password) {
        String loginToken = User.login(username, password);

        if (loginToken != null) {
            return "{\"loginToken\": \"" + loginToken + "\"}";
        }
        return null;
    }
}
