package com.webdesign.countryservice.model;

import java.time.LocalDateTime;

public class CacheEntry {
    private final String data;
    private final LocalDateTime expiry;

    public CacheEntry(String data, LocalDateTime expiry) {
        this.data = data;
        this.expiry = expiry;
    }

    public String getData() {
        return data;
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiry);
    }
}