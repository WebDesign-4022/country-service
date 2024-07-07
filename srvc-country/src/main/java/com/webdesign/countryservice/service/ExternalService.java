package com.webdesign.countryservice.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.webdesign.countryservice.exception.HttpCustomException;
import org.springframework.http.HttpStatus;

public class ExternalService {
    public static void authorizeToken(String apiToken) throws HttpCustomException {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get("http://srvc-users:8080/user/validate-token").header("Authorization", apiToken).asString();

            if (response.getStatus() == 401) {
                throw new HttpCustomException(HttpStatus.UNAUTHORIZED, "Invalid api token");
            } else if (response.getStatus() != 200) {
                throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
            }
        } catch (UnirestException e) {
            System.out.println("details of unexpected error: " + e.getMessage());
            throw new HttpCustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in validating token: " + e.getMessage());
        }
    }
}
