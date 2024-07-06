package com.webdesign.countryservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiToken {
    private final String name;
    private final String token;
    private final LocalDateTime expires;
    private boolean revoked;

    private static final List<ApiToken> apiTokens = new ArrayList<>();

    public ApiToken(String name, LocalDateTime expires) {
        this.name = name;
        this.token = "API " + UUID.randomUUID();
        this.expires = expires;
        this.revoked = false;
        apiTokens.add(this);
    }

    public String getRawToken() {
        return token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public String getName() {
        return name;
    }

    public void revoke() {
        this.revoked = true;
    }

    public boolean isValid() {
        return !revoked && expires.isAfter(LocalDateTime.now());
    }

    public static ApiToken getToken(String token) {
        for (ApiToken apiToken : apiTokens) {
            if (apiToken.getRawToken().equals(token)) {
                return apiToken;
            }
        }
        return null;
    }
}
