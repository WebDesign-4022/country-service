package com.webdesign.countryservice.service;

import com.webdesign.countryservice.exception.HttpCustomException;
import com.webdesign.countryservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    public String register(String username, String password) throws HttpCustomException {
        if (User.getUserByName(username) != null) {
            throw new HttpCustomException(HttpStatus.CONFLICT, "User already exists");
        }

        new User(username, password);
        return "{\"success\": true}";
    }

    public String login(String username, String password) throws HttpCustomException {
        String loginToken = User.login(username, password);

        if (loginToken != null) {
            return "{\"loginToken\": \"" + loginToken + "\"}";
        }
        throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
}
