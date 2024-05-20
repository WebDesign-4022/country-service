package com.webdesign.countryservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private int id;

    private final String name;
    private final String password;
    private boolean active;

    private String loginToken;
    private LocalDateTime loginTokenExpires;

    private List<Token> apiTokens = new ArrayList<>();
    private static final ArrayList<User> users = new ArrayList<>();

    public User(String name, String password) {
        this.id = users.size();
        this.name = name;
        this.password = password;
        this.active = false;
        users.add(this);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTokenValid() {
        if (loginTokenExpires == null) {
            return false;
        }
        return loginTokenExpires.isAfter(LocalDateTime.now());
    }


    public static User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByLoginToken(String loginToken) {
        for (User user : users) {
            if (user.loginToken != null && user.loginToken.equals(loginToken) && user.isTokenValid()) {
                return user;
            }
        }
        return null;
    }

    public static String login(String name, String password) {
        for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password) && user.active) {
                if (user.loginToken == null || !user.isTokenValid()) {
                    user.loginToken = "LOGIN " + UUID.randomUUID();
                    user.loginTokenExpires = LocalDateTime.now().plusHours(1);
                }
                return user.loginToken;
            }
        }
        return null;
    }

    public static boolean authenticate(String loginToken) {
        for (User user : users) {
            if (user.loginToken != null && user.loginToken.equals(loginToken) && user.active && user.isTokenValid()) {
                return true;
            }
        }
        return false;
    }
}
