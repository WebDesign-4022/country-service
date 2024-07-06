package com.webdesign.countryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpCustomException extends ResponseStatusException {
    public HttpCustomException(HttpStatus status, String reason) {
        super(status, reason);
    }
}